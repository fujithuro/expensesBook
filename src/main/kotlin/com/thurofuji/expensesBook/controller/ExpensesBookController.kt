package com.thurofuji.expensesBook.controller

import com.thurofuji.expensesBook.model.RequestedExpense
import com.thurofuji.expensesBook.model.ExpenseType
import com.thurofuji.expensesBook.model.ListSearchCondition
import com.thurofuji.expensesBook.service.ExpensesBookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * 家計簿に関するリクエストを受け付けるRestController
 */
@RestController
@RequestMapping("/api/expenseBook")
class ExpensesBookController(private val service: ExpensesBookService) {

    /**
     * 指定された条件に合致する出費（[RequestedExpense]）の[List]をレスポンスで返す
     *
     * パスパラメータ [yyyyMM]: 年月指定（yyyyMM形式）
     * クエリパラメータ [types]: 費目の絞り込み。複数指定可。省略可。
     */
    @GetMapping("/list/{yyyyMM}")
    fun getExpensesList(@PathVariable yyyyMM: String,
                        @RequestParam(required = false) types: List<Int> = emptyList()
    ): ResponseEntity<List<RequestedExpense>> {
        return tryToCreateCondition(yyyyMM, types)
            .map { service.findList(it) }
            .fold(
                onSuccess = { ok(it) },
                onFailure = { badRequest() }
            )
    }

    /**
     * 指定された[id]に合致する出費（[RequestedExpense]）を取得する。
     *
     * 該当するものが見つかれば`200 OK`としてレスポンスボディで詳細を返す。
     * 該当するものがなければ`404 Not Found`を返す。
     */
    @GetMapping("/detail/{id}")
    fun getExpensesDetail(@PathVariable id: UUID): ResponseEntity<RequestedExpense> {
        return service.findDetail(id)
            ?.let { ok(it) }
            ?: notFound()
    }

    /**
     * 出費を新規登録する
     *
     * TODO パラメータに対する入力値検証を追加する
     * TODO 登録に失敗した場合の処理は必要ないか？
     */
    @PostMapping
    fun registerExpense(@Valid @RequestBody expense: RequestedExpense): ResponseEntity<RequestedExpense> {
        // 入力値検証
        // TODO この`type`をサービスに渡すのは、Issue #8 出費情報を扱うモデルの整理 対応時に行う
        val type = kotlin.runCatching { expense.type!!.toExpenseType() }
            .getOrElse { return badRequest() }

        // 出費の登録
        val registered: RequestedExpense = service.register(expense)

        return created(registered)
    }

    /**
     * 指定された[id]の出費情報を[expense]の内容に更新する
     *
     * 更新が成功した場合には`204 No Content`を返す。
     * 指定された[id]の出費が存在しないなど、更新できなかった場合には`404 Not Found`を返す。新規登録は行わない。
     *
     * TODO パラメータに対する入力値検証を追加する
     */
    @PutMapping("/{id}")
    fun updateExpense(@PathVariable id: UUID, @Valid @RequestBody expense: RequestedExpense): ResponseEntity<Void> {
        // 入力値検証
        // TODO この`type`をサービスに渡すのは、Issue #8 出費情報を扱うモデルの整理 対応時に行う
        val type = kotlin.runCatching { expense.type!!.toExpenseType() }
            .getOrElse { return badRequest() }

        // 出費の更新
        val updatedRows = service.update(expense.copy(id = id))
        return if (updatedRows > 0) {
            noContent()
        } else {
            notFound()
        }
    }

    /**
     * 指定された[id]の出費情報を削除する
     *
     * 更新が成功した場合には`204 No Content`を返す。
     * 指定された[id]の出費が存在しないなど、削除できなかった場合には`404 Not Found`を返す。
     */
    @DeleteMapping("/{id}")
    fun deleteExpense(@PathVariable id: UUID): ResponseEntity<Void> {
        val deletedRows = service.delete(id)
        return if (deletedRows > 0) {
            noContent()
        } else {
            notFound()
        }
    }

    /**
     * リクエストされた情報（[yyyyMM]と[types]）から、出費の一覧を検索するための条件（[ListSearchCondition]）を作成した結果を返す
     */
    private fun tryToCreateCondition(yyyyMM: String, types: List<Int>): Result<ListSearchCondition> {
        val targetYearMonth: YearMonth = yyyyMM.parseYearMonth()
            .getOrElse { return Result.failure(it) }

        val typeList: List<ExpenseType> = runCatching { types.map { it.toExpenseType() } }
            .getOrElse { return Result.failure(it) }

        return Result.success(ListSearchCondition(targetYearMonth, typeList))
    }

    /**
     * 文字列を`yyyyMM`形式の年月としてパースした結果を、[YearMonth]の[Result]として返す
     */
    private fun String.parseYearMonth(): Result<YearMonth> = this.runCatching {
        YearMonth.parse(this, DateTimeFormatter.ofPattern("yyyyMM"))
    }

    /**
     * 出費の費目を表す[Int]を、列挙型である[ExpenseType]に変換する
     *
     * @throws IllegalArgumentException 列挙型に変換できなかった場合にスローされる
     */
    private fun Int.toExpenseType(): ExpenseType = ExpenseType.valueOf(this)

    /**
     * リクエストされた情報が不正で例外がスローされた場合のハンドリングを行う
     *
     * [IllegalArgumentException]: パラメータに不正があった場合全般にスローされる
     * [MethodArgumentTypeMismatchException]: パラメータの型が不正な場合にスローされる
     * [MethodArgumentNotValidException]: メソッドの引数に対する入力値検証で問題が見つかった場合にスローされる
     * [HttpMessageNotReadableException]: リクエスト内容をオブジェクトに展開できなかった場合にスローされる。たとえばnull非許容のプロパティにnullが送信された、型が合致しないなど
     *
     * TODO まだ入力値検証をほとんど実装していないので、実装後に必要な例外ハンドリングの精査が必要
     */
    @ExceptionHandler(
        IllegalArgumentException::class
        , MethodArgumentTypeMismatchException::class
        , MethodArgumentNotValidException::class
        , HttpMessageNotReadableException::class
    )
    fun handleException(ex: Exception): ResponseEntity<Map<String, String>> {
        return badRequest()
    }

    /**
     * `200 OK`を表す[ResponseEntity]を返す。レスポンスボディに含める情報は[body]に設定する。
     */
    private fun <T> ok(body: T? = null): ResponseEntity<T> = ResponseEntity.ok(body)

    /**
     * `201 Created`を表す[ResponseEntity]を返す。レスポンスボディに含める情報は[body]に設定する。
     */
    private fun <T> created(body: T? = null): ResponseEntity<T> = ResponseEntity(body, HttpStatus.CREATED)

    /**
     * `204 No Content`を表す[ResponseEntity]を返す。レスポンスボディに含める情報は[body]に設定する。
     */
    private fun <T> noContent(body: T? = null): ResponseEntity<T> = ResponseEntity(body, HttpStatus.NO_CONTENT)

    /**
     * `400 Bad Request`を表す[ResponseEntity]を返す。レスポンスボディに含める情報は[body]に設定する。
     */
    private fun <T> badRequest(body: T? = null): ResponseEntity<T> = ResponseEntity(body, HttpStatus.BAD_REQUEST)

    /**
     * `404 Not Found`を表す[ResponseEntity]を返す。レスポンスボディに含める情報は[body]に設定する。
     */
    private fun <T> notFound(body: T? = null): ResponseEntity<T> = ResponseEntity(body, HttpStatus.NOT_FOUND)

}
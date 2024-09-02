package com.thurofuji.expensesBook.controller

import com.thurofuji.expensesBook.bxo.toDto
import com.thurofuji.expensesBook.bxo.toNewDto
import com.thurofuji.expensesBook.bxo.toResponse
import com.thurofuji.expensesBook.model.ExpenseResponse
import com.thurofuji.expensesBook.model.ExpenseRequest
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
     * 指定された条件に合致する出費（[ExpenseResponse]）の[List]をレスポンスで返す
     *
     * パスパラメータ [yyyyMM]: 年月指定（yyyyMM形式）
     * クエリパラメータ [types]: 費目の絞り込み。複数指定可。省略可。
     */
    @GetMapping("/list/{yyyyMM}")
    fun getExpensesList(@PathVariable yyyyMM: String,
                        @RequestParam(required = false) types: List<Int> = emptyList()
    ): ResponseEntity<List<ExpenseResponse>> {
        return tryToCreateCondition(yyyyMM, types)
            .map { service.findList(it) }
            .fold(
                onSuccess = { list ->
                    val expenseList = list.map { it.toResponse() }
                    ok(expenseList)
                },
                onFailure = { badRequest() }
            )
    }

    /**
     * 指定された[id]に合致する出費（[ExpenseResponse]）を取得する。
     *
     * 該当するものが見つかれば`200 OK`としてレスポンスボディで詳細を返す。
     * 該当するものがなければ`404 Not Found`を返す。
     */
    @GetMapping("/detail/{id}")
    fun getExpensesDetail(@PathVariable id: UUID): ResponseEntity<ExpenseResponse> {
        return service.findDetail(id)
            ?.let { ok(it.toResponse()) }
            ?: notFound()
    }

    /**
     * 出費を新規登録する
     */
    @PostMapping
    fun registerExpense(@Valid @RequestBody request: ExpenseRequest): ResponseEntity<ExpenseResponse> {
        return runCatching { request.toNewDto() }
            .map { service.register(it) }
            .fold(
                onSuccess = { created(it.toResponse()) },
                onFailure = { badRequest() }
            )
    }

    /**
     * 指定された[id]の出費情報を[request]の内容に更新する
     *
     * 更新が成功した場合には`204 No Content`を返す。
     * 指定された[id]の出費が存在しないなど、更新できなかった場合には`404 Not Found`を返す。新規登録は行わない。
     */
    @PutMapping("/{id}")
    fun updateExpense(@PathVariable id: UUID, @Valid @RequestBody request: ExpenseRequest): ResponseEntity<Void> {
        return runCatching { request.toDto(id) }
            .map { service.update(it) }
            .fold(
                onSuccess = { updatedRows: Int -> when {
                    updatedRows > 0 -> noContent()
                    else -> notFound()
                }},
                onFailure = { badRequest() }
            )
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
        val targetYearMonth: YearMonth = runCatching { YearMonth.parse(yyyyMM, DateTimeFormatter.ofPattern("yyyyMM")) }
            .getOrElse { return Result.failure(it) }

        val typeList: List<ExpenseType> = runCatching { types.map { ExpenseType.valueOf(it) } }
            .getOrElse { return Result.failure(it) }

        return Result.success(ListSearchCondition(targetYearMonth, typeList))
    }

    /**
     * リクエストされた情報が不正で例外がスローされた場合のハンドリングを行う。
     * この例外ハンドラでは「引数の型やフォーマットが一致せず、値を設定できなかった」などエンドポイントである関数に処理が移る前の段階で例外が発生した場合のハンドリングを想定している。
     * 関数の中で発生した例外については、各関数で適宜処理されること。
     *
     * [MethodArgumentTypeMismatchException]: パラメータの型が不正な場合にスローされる
     * [MethodArgumentNotValidException]: メソッドの引数に対する入力値検証で問題が見つかった場合にスローされる
     * [HttpMessageNotReadableException]: リクエスト内容をオブジェクトに展開できなかった場合にスローされる。たとえばnull非許容のプロパティにnullが送信された、型が合致しないなど
     */
    @ExceptionHandler(
        MethodArgumentTypeMismatchException::class
        , MethodArgumentNotValidException::class
        , HttpMessageNotReadableException::class
    )
    fun handleValidationException(ex: Exception): ResponseEntity<Void> {
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
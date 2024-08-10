package com.thurofuji.expensesBook.controller

import com.thurofuji.expensesBook.model.Expense
import com.thurofuji.expensesBook.service.ExpensesBookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
import java.time.format.DateTimeParseException
import java.util.UUID

/**
 * 家計簿に関するリクエストを受け付けるRestController
 */
@RestController
@RequestMapping("/api/expenseBook")
class ExpensesBookController(private val service: ExpensesBookService) {

    /**
     * 指定された条件に合致する出費（[Expense]）の[List]をレスポンスで返す
     *
     * パスパラメータ [yyyyMM]: 年月指定（yyyyMM形式）
     * クエリパラメータ [types]: 費目の絞り込み。複数指定可。省略可。
     */
    @GetMapping("/list/{yyyyMM}")
    fun getExpensesList(@PathVariable yyyyMM: String,
                        @RequestParam(required = false) types: List<Int>?): ResponseEntity<List<Expense>> {
        val yearMonth: YearMonth = YearMonth.parse(yyyyMM, DateTimeFormatter.ofPattern("yyyyMM"))

        val list = service.findList(yearMonth, types ?: emptyList())

        return ok(list)
    }

    // TODO id指定された出費1件の取得を実装

    /**
     * 出費を新規登録する
     *
     * TODO パラメータに対する入力値検証を追加する
     * TODO 登録に失敗した場合の処理は必要ないか？
     */
    @PostMapping
    fun registerExpense(@RequestBody expense: Expense): ResponseEntity<Expense> {
        val registered: Expense = service.register(expense)

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
    fun updateExpense(@PathVariable id: UUID, @RequestBody expense: Expense): ResponseEntity<Void> {
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
     * リクエストされた情報が不正で例外がスローされた場合のハンドリングを行う
     *
     * [DateTimeParseException]: パラメータの日付指定に誤りがある場合にスローされる
     * [IllegalArgumentException]: パラメータに不正があった場合全般にスローされる
     * [MethodArgumentTypeMismatchException]: パラメータの型が不正な場合にスローされる
     *
     * TODO まだ入力値検証をほとんど実装していないので、実装後に必要な例外ハンドリングの精査が必要
     * TODO ひとまず例外のハンドリングが行えるようにしているだけで、レスポンスのメッセージ内容は暫定的なものなので、追って修正する
     * TODO レスポンス内容は暫定的にMapにしているが、これは専用クラスに置き換えたい（CUD用のresponseクラスなど）
     */
    @ExceptionHandler(
        DateTimeParseException::class
        , IllegalArgumentException::class
        , MethodArgumentTypeMismatchException::class
    )
    fun handleException(ex: Exception): ResponseEntity<Map<String, String>> {
        return badRequest(
            mapOf(
                "result" to "NG"
                , "error" to "Type mismatch error: ${ex.message}"
            )
        )
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
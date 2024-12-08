package com.thurofuji.expensesBook.controller

import com.thurofuji.expensesBook.annotation.ValidExpenseType
import com.thurofuji.expensesBook.annotation.ValidYearMonth
import com.thurofuji.expensesBook.bean.ExpenseRequest
import com.thurofuji.expensesBook.bean.ExpenseResponse
import com.thurofuji.expensesBook.service.ExpensesBookService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * 家計簿に関するリクエストを受け付けるRestController
 */
@RestController
@RequestMapping("/api/expenseBook")
class ExpensesBookController(private val service: ExpensesBookService) {

    /**
     * 指定された条件に合致する出費（[ExpenseResponse]）の[List]を取得するエンドポイント。
     *
     * * パスパラメータ [yyyyMM]: 年月指定（yyyyMM形式）
     * * クエリパラメータ [types]: 費目の絞り込み。複数指定可。省略された場合は費目での絞り込みを行わない
     *
     * 取得できれば`200 OK`でリストを返す。
     * 該当する出費が1件もない場合、`200 OK`で空のリストを返す。
     */
    @Suppress("UNUSED")
    @GetMapping("/list/{yyyyMM}")
    fun getExpensesList(@PathVariable
                        @ValidYearMonth
                        yyyyMM: String,
                        @RequestParam(required = false)
                        @ValidExpenseType
                        types: List<Int> = emptyList()
    ): ResponseEntity<List<ExpenseResponse>> {
        return service.findList(yyyyMM, types)
            .map(::ExpenseResponse)
            .let { ok(it) }
    }

    /**
     * 指定された[id]に合致する出費（[ExpenseResponse]）を取得するエンドポイント。
     *
     * 出費が存在すれば`200 OK`でその詳細を返し、存在しなければ`404 Not Found`を返す。
     */
    @Suppress("UNUSED")
    @GetMapping("/detail/{id}")
    fun getExpensesDetail(@PathVariable id: UUID): ResponseEntity<ExpenseResponse> {
        return service.findDetail(id)
            ?.let { ok(ExpenseResponse(it)) }
            ?: notFound()
    }

    /**
     * リクエストされた出費（[request]）を新規登録するエンドポイント。
     * [jwt]は認証済みのJWTトークン。
     *
     * 登録に成功した場合は`201 Created`で登録された出費を返す。
     */
    @Suppress("UNUSED")
    @PostMapping
    fun registerExpense(@Valid @RequestBody request: ExpenseRequest,
                        @AuthenticationPrincipal jwt: Jwt): ResponseEntity<ExpenseResponse> {
        val registered = service.register(request, jwt.subject)

        return created(ExpenseResponse(registered))
    }

    /**
     * 指定された[id]の出費情報を[request]の内容に更新するエンドポイント。
     * [jwt]は認証済みのJWTトークン。
     *
     * 更新が成功した場合には`204 No Content`を返しす。
     * 指定された[id]の出費が存在しない場合は`404 Not Found`を返し、新規登録は行わない。
     */
    @Suppress("UNUSED")
    @PutMapping("/{id}")
    fun updateExpense(@PathVariable id: UUID,
                      @Valid @RequestBody request: ExpenseRequest,
                      @AuthenticationPrincipal jwt: Jwt): ResponseEntity<Void> {
        val updatedRows = service.update(id, request, jwt.subject)
        return if (updatedRows > 0) {
            noContent()
        } else {
            notFound()
        }
    }

    /**
     * 指定された[id]の出費情報を削除するエンドポイント。
     *
     * 更新が成功した場合には`204 No Content`を返す。
     * 指定された[id]の出費が存在しなかった場合は`404 Not Found`を返す。
     */
    @Suppress("UNUSED")
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
     * `404 Not Found`を表す[ResponseEntity]を返す。レスポンスボディに含める情報は[body]に設定する。
     */
    private fun <T> notFound(body: T? = null): ResponseEntity<T> = ResponseEntity(body, HttpStatus.NOT_FOUND)

}

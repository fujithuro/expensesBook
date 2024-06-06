package com.thurofuji.expensesBook.controller

import com.thurofuji.expensesBook.model.ExpenseRequest
import com.thurofuji.expensesBook.service.ExpensesBookService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
 * 家計簿に関するリクエストを受け付けるRestController
 */
@RestController
@RequestMapping("/api/expenseBook")
class ExpensesBookController(private val expenseBookService: ExpensesBookService) {

    @GetMapping
    fun getExpensesList(@RequestParam(name = "name") name: String): String {
        // 現状はリクエストを受け取れていることを確認するだけの関数
        return ""
    }

    /**
     * POSTで送信された出費を登録する
     *
     * TODO パラメータに対する入力値検証を追加する
     */
    @PostMapping
    fun registerExpense(@RequestBody expense: ExpenseRequest) {
        // とりあえずパラメータを受け取れたことを確認するために標準出力している
        println("きたのは、${expense.date}, ${expense.price}, ${expense.store}, ${expense.type} です")

        expenseBookService.register(expense)
    }

    /**
     * パラメータの型不一致により[MethodArgumentTypeMismatchException]がスローされた場合のハンドリング
     * TODO ひとまず例外のハンドリングが行えるようにしているだけで、レスポンスのメッセージ内容は暫定的なものなので、追って修正する
     * TODO レスポンス内容は暫定的にMapにしているが、これは専用クラスに置き換えたい（CUD用のresponseクラスなど）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(
            mapOf(
                "result" to "NG"
                , "error" to "Type mismatch error: ${ex.message}"
            )
            , HttpStatus.BAD_REQUEST
        )
    }

}
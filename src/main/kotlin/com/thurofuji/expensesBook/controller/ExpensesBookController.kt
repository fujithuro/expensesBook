package com.thurofuji.expensesBook.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ExpensesBookController {

    @GetMapping
    fun getExpensesList(@RequestParam(name = "name") name: String): String {
        // 現状はリクエストを受け取れていることを確認するだけの関数
        return ""
    }

}
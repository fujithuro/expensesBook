package com.thurofuji.expensesBook.bean

import com.thurofuji.expensesBook.annotation.ValidExpenseType
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

/**
 * リクエストされた出費を表すデータクラス
 */
data class ExpenseRequest(
    @field:NotNull
    val date: LocalDate?,
    @field:NotNull
    val price: Int?,
    val store: String = "",
    val usage: String = "",
    @field:ValidExpenseType
    @field:NotNull
    val type: Int?
)
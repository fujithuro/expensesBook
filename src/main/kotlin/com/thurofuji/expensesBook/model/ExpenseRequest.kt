package com.thurofuji.expensesBook.model

import java.time.LocalDate

/**
 * リクエストに含まれる出費の情報
 */
data class ExpenseRequest(
    val date: LocalDate,
    val price: Int,
    val store: String = "",
    val type: Int
)

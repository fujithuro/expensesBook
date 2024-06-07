package com.thurofuji.expensesBook.model

import java.time.LocalDate
import java.util.UUID

/**
 * 出費の情報を表すデータクラス
 */
data class Expense(
    val id: UUID? = null,
    val date: LocalDate,
    val price: Int,
    val store: String = "",
    val type: Int
)

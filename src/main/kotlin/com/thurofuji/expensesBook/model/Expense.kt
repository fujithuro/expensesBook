package com.thurofuji.expensesBook.model

import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.util.UUID

/**
 * 出費の情報を表すデータクラス
 */
data class Expense(
    val id: UUID? = null,
    @field:NotNull val date: LocalDate?,
    @field:NotNull val price: Int?,
    val store: String = "",
    val usage: String = "",
    @field:NotNull val type: Int?
)

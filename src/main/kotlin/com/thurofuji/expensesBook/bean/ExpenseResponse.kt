package com.thurofuji.expensesBook.bean

import java.time.LocalDate
import java.util.UUID

/**
 * レスポンスで扱われる出費1件を表すためのモデル
 */
data class ExpenseResponse(val id: UUID,
                           val date: LocalDate,
                           val price: Int,
                           val store: String,
                           val usage: String,
                           val type: Int)
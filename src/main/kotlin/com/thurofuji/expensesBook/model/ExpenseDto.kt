package com.thurofuji.expensesBook.model

import java.time.LocalDate
import java.util.UUID

/**
 * 主にServiceの層で、DBに登録済みの出費1件を扱うためのモデル
 */
data class ExpenseDto(val id: UUID,
                      val 支払日: LocalDate,
                      val 費目: ExpenseType,
                      val 金額: Int,
                      val 支払先: String,
                      val 使途: String)
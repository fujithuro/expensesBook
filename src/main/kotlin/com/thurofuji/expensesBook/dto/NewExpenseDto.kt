package com.thurofuji.expensesBook.dto

import java.time.LocalDate

/**
 * 主にServiceの層で、まだDBに登録されていない出費1件を扱うためのモデル
 */
data class NewExpenseDto(val 支払日: LocalDate
                         , val 費目: ExpenseType
                         , val 金額: Int
                         , val 支払先: String
                         , val 使途: String
                         , val 登録者id: Int)
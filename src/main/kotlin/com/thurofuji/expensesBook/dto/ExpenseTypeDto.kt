package com.thurofuji.expensesBook.dto

/**
 * 主にService層で費目1件を扱うためのモデル
 */
data class ExpenseTypeDto(val 費目cd: Int
                          , val 名称: String
                          , val is有効: Boolean)
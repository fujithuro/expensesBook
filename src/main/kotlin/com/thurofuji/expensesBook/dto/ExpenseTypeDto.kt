package com.thurofuji.expensesBook.dto

import com.thurofuji.expensesBook.entity.費目マスター

/**
 * 主にService層で費目1件を扱うためのモデル
 */
data class ExpenseTypeDto(val 費目cd: Int
                          , val 名称: String
                          , val is有効: Boolean) {
    constructor(entity: 費目マスター): this(entity.費目cd, entity.費目名, entity.有効区分)
}
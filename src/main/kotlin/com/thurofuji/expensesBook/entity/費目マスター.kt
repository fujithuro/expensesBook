package com.thurofuji.expensesBook.entity

/**
 * テーブル「費目マスター」のレコード1件を表すモデル
 */
data class 費目マスター(val 費目cd: Int
                      , val 費目名: String
                      , val 有効区分: Boolean) {
    companion object {
        const val 費目cd = "費目cd"
        const val 費目名 = "費目名"
        const val 有効区分 = "有効区分"
    }

}

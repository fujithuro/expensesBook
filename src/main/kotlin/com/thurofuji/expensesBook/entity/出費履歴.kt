package com.thurofuji.expensesBook.entity

import java.time.LocalDate
import java.util.UUID

/**
 * テーブル「出費履歴」のレコード1件を表すモデル
 */
data class 出費履歴(val id: UUID,
                    val 支払日: LocalDate,
                    val 費目cd: Int,
                    val 金額: Int,
                    val 支払先: String,
                    val 使途: String,
                    val 最終更新者id: Int) {

    companion object {
        const val id = "id"
        const val 支払日 = "支払日"
        const val 金額 = "金額"
        const val 支払先 = "支払先"
        const val 使途 = "使途"
        const val 費目cd = "費目cd"
        const val 最終更新者id = "最終更新者id"
        const val 最終更新日時 = "最終更新日時"
    }

}
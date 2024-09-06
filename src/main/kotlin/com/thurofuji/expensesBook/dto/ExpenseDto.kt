package com.thurofuji.expensesBook.dto

import java.time.LocalDate
import java.util.UUID

/**
 * 主にServiceの層で、DBに登録済みの出費1件を扱うためのモデル
 */
data class ExpenseDto(val id: UUID
                      , val 支払日: LocalDate
                      , val 費目: ExpenseTypeDto
                      , val 金額: Int
                      , val 支払先: String
                      , val 使途: String
                      , val 最終更新者id: Int) {
    constructor(id: UUID, newDto: NewExpenseDto): this(
        id, newDto.支払日, newDto.費目, newDto.金額, newDto.支払先, newDto.使途, newDto.登録者id
    )
}
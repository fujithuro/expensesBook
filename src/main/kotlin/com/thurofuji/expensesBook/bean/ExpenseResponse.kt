package com.thurofuji.expensesBook.bean

import com.thurofuji.expensesBook.dto.ExpenseDto
import java.time.LocalDate
import java.util.UUID

/**
 * レスポンスで扱われる出費1件を表すためのモデル
 */
data class ExpenseResponse(val id: UUID
                           , val date: LocalDate
                           , val price: Int
                           , val store: String
                           , val usage: String
                           , val type: Int) {
    constructor(dto: ExpenseDto): this(
        dto.id, dto.支払日, dto.金額, dto.支払先, dto.使途, dto.費目.費目cd
    )
}
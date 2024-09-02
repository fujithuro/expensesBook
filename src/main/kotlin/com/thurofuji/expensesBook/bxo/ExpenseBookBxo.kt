package com.thurofuji.expensesBook.bxo

import com.thurofuji.expensesBook.model.ExpenseDto
import com.thurofuji.expensesBook.model.ExpenseResponse

/**
 * リクエストやレスポンスなどのController層で扱うモデルと、Dtoなどの主にService層で扱うモデルの変換を行う関数を集めたファイル
 */

/**
 * [ExpenseDto]を[ExpenseResponse]に変換する
 */
fun ExpenseDto.toResponse() = ExpenseResponse(
    id = id
    , date = 支払日
    , price = 金額
    , store = 支払先
    , usage = 使途
    , type = 費目.code
)
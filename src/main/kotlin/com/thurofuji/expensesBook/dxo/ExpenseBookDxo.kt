package com.thurofuji.expensesBook.dxo

import com.thurofuji.expensesBook.model.ExpenseDto
import com.thurofuji.expensesBook.model.ExpenseType
import com.thurofuji.expensesBook.model.出費履歴

/**
 * Dtoなどの主にService層で扱うモデルと、DBのテーブルなど主にRepository層で扱うモデルの変換を行う関数を集めたファイル
 */

/**
 * [出費履歴]を[ExpenseDto]へ変換する
 */
fun 出費履歴.toDto() = ExpenseDto(
    id
    , 支払日
    , ExpenseType.valueOf(費目cd)
    , 金額
    , 支払先
    , 使途
)
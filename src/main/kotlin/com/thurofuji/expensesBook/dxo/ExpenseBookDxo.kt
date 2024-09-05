package com.thurofuji.expensesBook.dxo

import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.dto.ExpenseType
import com.thurofuji.expensesBook.dto.NewExpenseDto
import com.thurofuji.expensesBook.entity.出費履歴
import java.util.UUID

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
    , 最終更新者id
)

/**
 * [NewExpenseDto]を[ExpenseDto]へ変換する
 * [ExpenseDto.id]には[id]が使用される
 */
fun NewExpenseDto.toDto(id: UUID) = ExpenseDto(
    id
    , 支払日
    , 費目
    , 金額
    , 支払先
    , 使途
    , 登録者id
)
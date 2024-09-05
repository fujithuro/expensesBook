package com.thurofuji.expensesBook.dxo

import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.dto.ExpenseType
import com.thurofuji.expensesBook.dto.ExpenseTypeDto
import com.thurofuji.expensesBook.dto.NewExpenseDto
import com.thurofuji.expensesBook.entity.出費履歴
import com.thurofuji.expensesBook.entity.費目マスター
import java.util.UUID

/**
 * Dtoなどの主にService層で扱うモデルと、DBのテーブルなど主にRepository層で扱うモデルの変換を行う関数を集めたファイル
 */

/**
 * [出費履歴]を[ExpenseDto]へ変換する
 */
fun 出費履歴.toDto() = ExpenseDto(
    id = id
    , 支払日 = 支払日
    , 費目 = ExpenseType.valueOf(費目cd)
    , 金額 = 金額
    , 支払先 = 支払先
    , 使途 = 使途
    , 最終更新者id = 最終更新者id
)

/**
 * [費目マスター]を[ExpenseTypeDto]へ変換する
 */
fun 費目マスター.toDto() = ExpenseTypeDto(
    費目cd = 費目cd
    , 名称 = 費目名
    , is有効 = 有効区分
)

/**
 * [NewExpenseDto]を[ExpenseDto]へ変換する
 * [ExpenseDto.id]には[id]が使用される
 */
fun NewExpenseDto.toDto(id: UUID) = ExpenseDto(
    id = id
    , 支払日 = 支払日
    , 費目 = 費目
    , 金額 = 金額
    , 支払先 = 支払先
    , 使途 = 使途
    , 最終更新者id = 登録者id
)
package com.thurofuji.expensesBook.bxo

import com.thurofuji.expensesBook.bean.ExpenseRequest
import com.thurofuji.expensesBook.bean.ExpenseResponse
import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.dto.ExpenseType
import com.thurofuji.expensesBook.dto.NewExpenseDto
import java.util.UUID

/**
 * リクエストやレスポンスなどのController層で扱うモデルと、Dtoなどの主にService層で扱うモデルの変換を行う関数を集めたファイル
 */

/**
 * [ExpenseRequest]を[NewExpenseDto]へ変換する
 */
fun ExpenseRequest.toNewDto() = NewExpenseDto(
    // 支払日,費目,金額はアノテーションでnullでないことを確認している
    支払日 = date!!
    , 費目 = ExpenseType.valueOf(type!!)
    , 金額 = price!!
    , 支払先 = store
    , 使途 = usage
)

/**
 * [ExpenseRequest]を[ExpenseDto]へ変換する
 * [ExpenseDto.id]には[id]が設定される
 */
fun ExpenseRequest.toDto(id: UUID) = ExpenseDto(
    // 支払日,費目,金額はアノテーションでnullでないことを確認している
    id = id
    , 支払日 = date!!
    , 費目 = ExpenseType.valueOf(type!!)
    , 金額 = price!!
    , 支払先 = store
    , 使途 = usage
)

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
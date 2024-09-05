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
 * [ExpenseRequest]を[NewExpenseDto]へ変換する。
 * [NewExpenseDto.登録者id]には[userId]を設定する
 */
fun ExpenseRequest.toNewDto(userId: String) = NewExpenseDto(
    // 支払日,費目,金額はアノテーションでnullでないことを確認している
    支払日 = date!!
    , 費目 = ExpenseType.valueOf(type!!)
    , 金額 = price!!
    , 支払先 = store
    , 使途 = usage
    , 登録者id = userId.toInt()
)

/**
 * [ExpenseRequest]を[ExpenseDto]へ変換する。
 * [ExpenseDto.id]には[id]が設定される。
 * [ExpenseDto.最終更新者id]には[userId]が設定される。
 */
fun ExpenseRequest.toDto(id: UUID, userId: String) = ExpenseDto(
    // 支払日,費目,金額はアノテーションでnullでないことを確認している
    id = id
    , 支払日 = date!!
    , 費目 = ExpenseType.valueOf(type!!)
    , 金額 = price!!
    , 支払先 = store
    , 使途 = usage
    , 最終更新者id = userId.toInt()
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
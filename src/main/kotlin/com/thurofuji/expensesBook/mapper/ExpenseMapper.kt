package com.thurofuji.expensesBook.mapper

import com.thurofuji.expensesBook.bean.ExpenseRequest
import com.thurofuji.expensesBook.bean.ExpenseResponse
import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.dto.ExpenseTypeDto
import com.thurofuji.expensesBook.dto.ListSearchCondition
import com.thurofuji.expensesBook.dto.NewExpenseDto
import com.thurofuji.expensesBook.entity.出費履歴
import com.thurofuji.expensesBook.entity.費目マスター
import java.util.UUID

/**
 * 出費に関する情報を扱うモデルを、別のモデルに変換するMapper
 */
interface ExpenseMapper {

    /**
     * 対象年月（[yyyyMM]）と費目（[types]）から[ListSearchCondition]を作成する
     */
    fun toSearchCondition(yyyyMM: String, types: List<Int>): ListSearchCondition

    /**
     * [request]から[NewExpenseDto]を作成する。
     * [userId]は[NewExpenseDto.登録者id]に設定される。
     */
    fun toNewDto(userId: String, request: ExpenseRequest): NewExpenseDto

    /**
     * [request]から[ExpenseDto]を作成する。
     * [userId]は[ExpenseDto.最終更新者id]に、[id]は[ExpenseDto.id]に設定される。
     */
    fun toDto(userId: String, id: UUID, request: ExpenseRequest): ExpenseDto

    /**
     * [dto]から[ExpenseResponse]を作成する
     */
    fun toResponse(dto: ExpenseDto): ExpenseResponse

    /**
     * [entity]から[ExpenseDto]を作成する
     */
    fun toDto(entity: 出費履歴): ExpenseDto

    /**
     * [entity]から[ExpenseTypeDto]を作成する
     */
    fun toDto(entity: 費目マスター): ExpenseTypeDto

    /**
     * [newDto]を[ExpenseDto]に変換する。
     * [id]は[ExpenseDto.id]に設定される。
     */
    fun toDto(id: UUID, newDto: NewExpenseDto): ExpenseDto

}
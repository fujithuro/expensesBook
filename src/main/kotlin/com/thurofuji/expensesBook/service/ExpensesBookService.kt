package com.thurofuji.expensesBook.service

import com.thurofuji.expensesBook.bean.ExpenseRequest
import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.dto.ListSearchCondition
import com.thurofuji.expensesBook.dto.NewExpenseDto
import com.thurofuji.expensesBook.dxo.toDto
import com.thurofuji.expensesBook.mapper.ExpenseMapper
import com.thurofuji.expensesBook.repository.ExpenseBookRepository
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 家計簿のビジネスロジックを取り扱うServiceクラス
 */
@Service
class ExpensesBookService(private val expenseTypeService: ExpenseTypeService
                          , private val repository: ExpenseBookRepository
                          , private val mapper: ExpenseMapper
) {
    /**
     * [yyyyMM]や[types]で指定された条件に該当する出費一覧を取得した結果を返す。
     * 一覧取得に成功した場合には[ExpenseDto]の[List]を返し、失敗した場合には原因の[Throwable]を返す。
     */
    fun findList(yyyyMM: String, types: List<Int>): Result<List<ExpenseDto>> {
        return runCatching { mapper.toSearchCondition(yyyyMM, types) }
            .map { findList(it) }
    }

    /**
     * 指定された条件（[condition]）に該当する出費一覧を[ExpenseDto]の[List]で取得する
     */
    private fun findList(condition: ListSearchCondition): List<ExpenseDto> {
        return repository.findList(condition)
            .map { it.toDto(expenseTypeService.getExpenseType(it.費目cd)) }
    }

    /**
     * 指定された[id]に合致する出費（[ExpenseDto]）を取得する。
     * 該当するものがなければnullを返す。
     */
    fun findDetail(id: UUID): ExpenseDto? {
        return repository.findDetail(id)?.let { it.toDto(expenseTypeService.getExpenseType(it.費目cd)) }
    }

    /**
     * [request]で指定された出費の情報を[userId]の出費として登録した結果を返す。
     * 登録に成功した場合には登録された出費（[ExpenseDto]）を返し、失敗した場合には原因の[Throwable]を返す。
     */
    fun register(request: ExpenseRequest, userId: String): Result<ExpenseDto> {
        return runCatching { mapper.toNewDto(userId, request) }
            .map { register(it) }
    }

    /**
     * 出費情報（[expense]）を登録し、登録された出費（[ExpenseDto]）を返す
     */
    private fun register(expense: NewExpenseDto): ExpenseDto {
        return repository.register(expense)
    }

    /**
     * [id]で指定された出費を、[request]の内容へ[userId]の出費として更新した結果を返す。
     * 更新に成功した場合には更新された行数を返し、失敗した場合には原因の[Throwable]を返す。
     */
    fun update(id: UUID, request: ExpenseRequest, userId: String): Result<Int> {
        return runCatching { mapper.toDto(userId, id, request) }
            .map { update(it) }
    }

    /**
     * 既存の出費（[expense]）の内容を更新し、更新された行数を返す
     */
    private fun update(expense: ExpenseDto): Int {
        return repository.update(expense)
    }

    /**
     * [id]で指定された既存の出費を削除し、削除された行数を返す
     */
    fun delete(id: UUID): Int {
        return repository.delete(id)
    }

}
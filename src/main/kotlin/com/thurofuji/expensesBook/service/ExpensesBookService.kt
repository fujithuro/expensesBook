package com.thurofuji.expensesBook.service

import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.dto.ExpenseTypeDto
import com.thurofuji.expensesBook.dto.ListSearchCondition
import com.thurofuji.expensesBook.dto.NewExpenseDto
import com.thurofuji.expensesBook.dxo.toDto
import com.thurofuji.expensesBook.repository.ExpenseBookRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.UUID

/**
 * 家計簿のビジネスロジックを取り扱うServiceクラス
 */
@Service
class ExpensesBookService(private val expenseTypeService: ExpenseTypeService
                          , private val repository: ExpenseBookRepository) {

    /**
     * 指定された条件（[condition]）に該当する出費一覧を[ExpenseDto]の[List]で取得する
     */
    fun findList(condition: ListSearchCondition): List<ExpenseDto> {
        return repository.findList(
            condition.targetYearMonth.atDay(1)
            , condition.targetYearMonth.atEndOfMonth()
            , condition.typeList.map { it.費目cd }
        ).map { it.toDto(expenseTypeService.getExpenseType(it.費目cd)) }
    }

    /**
     * 指定された[id]に合致する出費（[ExpenseDto]）を取得する。
     * 該当するものがなければnullを返す。
     */
    fun findDetail(id: UUID): ExpenseDto? {
        return repository.findDetail(id)?.let { it.toDto(expenseTypeService.getExpenseType(it.費目cd)) }
    }

    /**
     * 出費情報（[expense]）を登録し、登録された出費（[ExpenseDto]）を返す
     */
    fun register(expense: NewExpenseDto): ExpenseDto {
        return repository.register(expense)
    }

    /**
     * 既存の出費（[expense]）の内容を更新し、更新された行数を返す
     */
    fun update(expense: ExpenseDto): Int {
        return repository.update(expense)
    }

    /**
     * [id]で指定された既存の出費を削除し、削除された行数を返す
     */
    fun delete(id: UUID): Int {
        return repository.delete(id)
    }


}
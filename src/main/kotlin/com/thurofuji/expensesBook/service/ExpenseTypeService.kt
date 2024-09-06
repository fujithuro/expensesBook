package com.thurofuji.expensesBook.service

import com.thurofuji.expensesBook.dto.ExpenseTypeDto
import com.thurofuji.expensesBook.mapper.ExpenseMapper
import com.thurofuji.expensesBook.repository.ExpenseBookRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * 費目に関するビジネスロジックを扱うServiceクラス
 */
@Service
class ExpenseTypeService(private val repository: ExpenseBookRepository
                         , private val mapper: ExpenseMapper) {

    /**
     * [code]に該当する有効な費目（[ExpenseTypeDto]）を取得する
     *
     * @throws IllegalArgumentException [code]に該当する有効な費目が見つからない場合にスローされる
     */
    @Cacheable("expenseTypes")
    fun getExpenseType(code: Int): ExpenseTypeDto = getValidExpenseTypes().firstOrNull { it.費目cd == code }
        ?: throw IllegalArgumentException("Invalid code for expense type.: $code")

    /**
     * 有効な費目の一覧を[ExpenseTypeDto]の[List]として取得する
     */
    private fun getValidExpenseTypes(): List<ExpenseTypeDto> {
        return repository.findExpenseTypeList().filter { it.有効区分 }.map { mapper.toDto(it) }
    }
}
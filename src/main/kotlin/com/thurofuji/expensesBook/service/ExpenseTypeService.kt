package com.thurofuji.expensesBook.service

import com.thurofuji.expensesBook.dto.ExpenseTypeDto
import com.thurofuji.expensesBook.entity.費目マスター
import com.thurofuji.expensesBook.exception.InvalidExpenseTypeException
import com.thurofuji.expensesBook.repository.ExpenseBookRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 費目に関するビジネスロジックを扱うServiceクラス
 */
@Service
class ExpenseTypeService(private val repository: ExpenseBookRepository) {

    /**
     * [code]に該当する有効な費目（[ExpenseTypeDto]）を取得する
     *
     * @throws InvalidExpenseTypeException [code]に該当する有効な費目が見つからない場合にスローされる
     */
    @Cacheable("expenseTypes")
    @Transactional(readOnly = true)
    fun getExpenseType(code: Int): ExpenseTypeDto = getValidExpenseTypes().firstOrNull { it.費目cd == code }
        ?: throw InvalidExpenseTypeException("Invalid code for expense type.: $code")

    /**
     * 有効な費目の一覧を[ExpenseTypeDto]の[List]として取得する
     */
    private fun getValidExpenseTypes(): List<ExpenseTypeDto> {
        return repository.findExpenseTypeList().filter(費目マスター::有効区分).map(::ExpenseTypeDto)
    }
}

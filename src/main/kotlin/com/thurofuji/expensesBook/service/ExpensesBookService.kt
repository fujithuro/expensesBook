package com.thurofuji.expensesBook.service

import com.thurofuji.expensesBook.model.ExpenseRequest
import com.thurofuji.expensesBook.repository.ExpenseBookRepository
import org.springframework.stereotype.Service

/**
 * 家計簿のビジネスロジックを取り扱うServiceクラス
 */
@Service
class ExpensesBookService(private val expenseBookRepository: ExpenseBookRepository) {

    /**
     * 出費情報（[expense]）を登録する
     */
    fun register(expense: ExpenseRequest) {
        expenseBookRepository.register(expense)
    }

}
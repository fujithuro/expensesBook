package com.thurofuji.expensesBook.service

import com.thurofuji.expensesBook.repository.ExpenseBookRepository
import org.springframework.stereotype.Service

/**
 * 家計簿のビジネスロジックを取り扱うServiceクラス
 */
@Service
class ExpensesBookService(private val expenseBookRepository: ExpenseBookRepository) {
    // TODO Controllerからの要求に応じた処理を実装
}
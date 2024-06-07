package com.thurofuji.expensesBook.service

import com.thurofuji.expensesBook.model.Expense
import com.thurofuji.expensesBook.repository.ExpenseBookRepository
import org.springframework.stereotype.Service
import java.time.YearMonth
import java.util.UUID

/**
 * 家計簿のビジネスロジックを取り扱うServiceクラス
 */
@Service
class ExpensesBookService(private val expenseBookRepository: ExpenseBookRepository) {

    /**
     * 指定された年月（[YearMonth]）の出費一覧を[Expense]の[List]で取得する
     */
    fun findList(yearMonth: YearMonth): List<Expense> {
        return expenseBookRepository.findList(
            yearMonth.atDay(1)
            , yearMonth.atEndOfMonth()
        )
    }

    /**
     * 出費情報（[expense]）を登録し、登録された出費のidを[UUID]で返す
     */
    fun register(expense: Expense): UUID {
        return expenseBookRepository.register(expense)
    }

}
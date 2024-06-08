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
class ExpensesBookService(private val repository: ExpenseBookRepository) {

    /**
     * 指定された年月（[YearMonth]）および費目の出費一覧を[Expense]の[List]で取得する
     */
    fun findList(yearMonth: YearMonth, typeList: List<Int>): List<Expense> {
        return repository.findList(
            yearMonth.atDay(1)
            , yearMonth.atEndOfMonth()
            , typeList
        )
    }

    /**
     * 出費情報（[expense]）を登録し、登録された出費（[Expense]）を返す
     */
    fun register(expense: Expense): Expense {
        return repository.register(expense)
    }

    /**
     * 既存の出費（[Expense]）の内容を更新し、更新された行数を返す
     */
    fun update(expense: Expense): Int {
        return repository.update(expense)
    }

    /**
     * [id]で指定された既存の出費を削除し、削除された行数を返す
     */
    fun delete(id: UUID): Int {
        return repository.delete(id)
    }

}
package com.thurofuji.expensesBook.mapper

import com.thurofuji.expensesBook.dto.ListSearchCondition
import com.thurofuji.expensesBook.service.ExpenseTypeService
import org.springframework.stereotype.Component
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * [ExpenseMapper]の標準実装
 */
@Component
class StandardExpenseMapper(private val expenseTypeService: ExpenseTypeService): ExpenseMapper {

    override fun toSearchCondition(yyyyMM: String, types: List<Int>): ListSearchCondition {
        val yearMonth = YearMonth.parse(yyyyMM, DateTimeFormatter.ofPattern("yyyyMM"))
        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()

        val typeList = types.map { expenseTypeService.getExpenseType(it) }.map { it.費目cd }

        return ListSearchCondition(start, end, typeList)
    }

}
package com.thurofuji.expensesBook.mapper

import com.thurofuji.expensesBook.bean.ExpenseRequest
import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.dto.ListSearchCondition
import com.thurofuji.expensesBook.dto.NewExpenseDto
import com.thurofuji.expensesBook.entity.出費履歴
import com.thurofuji.expensesBook.exception.InvalidTargetYearMonthException
import com.thurofuji.expensesBook.service.ExpenseTypeService
import org.springframework.stereotype.Component
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.UUID

/**
 * [ExpenseMapper]の標準実装
 */
@Component
class StandardExpenseMapper(private val expenseTypeService: ExpenseTypeService): ExpenseMapper {

    override fun toSearchCondition(yyyyMM: String, types: List<Int>): ListSearchCondition {
        val yearMonth = runCatching {
            YearMonth.parse(yyyyMM, DateTimeFormatter.ofPattern("yyyyMM"))
        }.getOrElse { throw InvalidTargetYearMonthException("Invalid value for YearMonth.: $yyyyMM") }

        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()

        val typeList = types.map { expenseTypeService.getExpenseType(it) }.map { it.費目cd }

        return ListSearchCondition(start, end, typeList)
    }

    override fun toNewDto(userId: String, request: ExpenseRequest): NewExpenseDto = NewExpenseDto(
        // リクエストの持つ支払日/費目コード/金額は、nullでないことをアノテーションで確認している
        支払日 = request.date!!
        , 費目 = expenseTypeService.getExpenseType(request.type!!)
        , 金額 = request.price!!
        , 支払先 = request.store
        , 使途 = request.usage
        , 登録者id = userId.toInt()
    )

    override fun toDto(userId: String, id: UUID, request: ExpenseRequest): ExpenseDto = ExpenseDto(
        // リクエストの持つ支払日/費目コード/金額は、nullでないことをアノテーションで確認している
        id = id
        , 支払日 = request.date!!
        , 費目 = expenseTypeService.getExpenseType(request.type!!)
        , 金額 = request.price!!
        , 支払先 = request.store
        , 使途 = request.usage
        , 最終更新者id = userId.toInt()
    )

    override fun toDto(entity: 出費履歴): ExpenseDto = ExpenseDto(
        id = entity.id
        , 支払日 = entity.支払日
        , 費目 = expenseTypeService.getExpenseType(entity.費目cd)
        , 金額 = entity.金額
        , 支払先 = entity.支払先
        , 使途 = entity.使途
        , 最終更新者id = entity.最終更新者id
    )

}
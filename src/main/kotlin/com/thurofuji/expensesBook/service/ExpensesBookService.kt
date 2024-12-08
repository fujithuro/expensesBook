package com.thurofuji.expensesBook.service

import com.thurofuji.expensesBook.bean.ExpenseRequest
import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.mapper.ExpenseMapper
import com.thurofuji.expensesBook.repository.ExpenseBookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * 家計簿のビジネスロジックを取り扱うServiceクラス
 */
@Service
class ExpensesBookService(private val repository: ExpenseBookRepository
                          , private val mapper: ExpenseMapper) {
    /**
     * [yyyyMM]や[types]で指定された条件に該当する出費一覧を、[ExpenseDto]の[List]として返す。
     */
    @Transactional(readOnly = true)
    fun findList(yyyyMM: String, types: List<Int>): List<ExpenseDto> {
        return repository.findList(mapper.toSearchCondition(yyyyMM, types))
            .map(mapper::toDto)
    }

    /**
     * 指定された[id]に合致する出費（[ExpenseDto]）を取得する。
     * 該当するものがなければnullを返す。
     */
    @Transactional(readOnly = true)
    fun findDetail(id: UUID): ExpenseDto? {
        return repository.findDetail(id)?.let(mapper::toDto)
    }

    /**
     * [request]で指定された出費の情報を[userId]の出費として登録する。
     * 戻り値として登録された出費（[ExpenseDto]）を返す。
     */
    @Transactional
    fun register(request: ExpenseRequest, userId: String): ExpenseDto {
        return repository.register(mapper.toNewDto(userId, request))
    }

    /**
     * [id]で指定された出費を、[request]の内容へ[userId]の出費として更新し、更新された行数を返す。
     */
    @Transactional
    fun update(id: UUID, request: ExpenseRequest, userId: String): Int {
        return repository.update(mapper.toDto(userId, id, request))
    }

    /**
     * [id]で指定された既存の出費を削除し、削除された行数を返す
     */
    @Transactional
    fun delete(id: UUID): Int {
        return repository.delete(id)
    }

}

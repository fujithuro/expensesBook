package com.thurofuji.expensesBook.repository

import com.thurofuji.expensesBook.model.ExpenseRequest
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.simple.JdbcClient
import java.util.UUID

/**
 * 家計簿に関する情報の永続化、および永続化された情報へのアクセスを行うRepositoryクラス
 */
@Repository
class ExpenseBookRepository(private val jdbcClient: JdbcClient) {
    // TODO JdbcClientを通してDBアクセス

    /**
     * 出費情報（[expense]）を永続化し、登録されたidを[UUID]で返す
     */
    fun register(expense: ExpenseRequest): UUID {
        return jdbcClient.sql("""
            INSERT INTO 出費 (購入日, 金額, 店舗, 費目cd)
            VALUES (?, ?, ?, ?)
            RETURNING id
        """.trimIndent())
            .params(
                expense.date
                , expense.price
                , expense.store
                , expense.type
            ).query(UUID::class.java)
            .single()
    }

}
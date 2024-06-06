package com.thurofuji.expensesBook.repository

import com.thurofuji.expensesBook.model.ExpenseRequest
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.simple.JdbcClient

/**
 * 家計簿に関する情報の永続化、および永続化された情報へのアクセスを行うRepositoryクラス
 */
@Repository
class ExpenseBookRepository(private val jdbcClient: JdbcClient) {
    // TODO JdbcClientを通してDBアクセス

    /**
     * 出費情報（[expense]）を永続化する
     */
    fun register(expense: ExpenseRequest) {
        // TODO 出費情報のINSERT
    }

}
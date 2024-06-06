package com.thurofuji.expensesBook.repository

import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.simple.JdbcClient

/**
 * 家計簿に関する情報の永続化、および永続化された情報へのアクセスを行うRepositoryクラス
 */
@Repository
class ExpenseBookRepository(private val jdbcClient: JdbcClient) {
    // TODO JdbcClientを通してDBアクセス
}
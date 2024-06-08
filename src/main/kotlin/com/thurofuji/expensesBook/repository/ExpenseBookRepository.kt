package com.thurofuji.expensesBook.repository

import com.thurofuji.expensesBook.model.Expense
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDate
import java.util.UUID

/**
 * 家計簿に関する情報の永続化、および永続化された情報へのアクセスを行うRepositoryクラス
 */
@Repository
class ExpenseBookRepository(private val jdbcClient: JdbcClient) {
    /**
     * [start]から[end]までの期間の出費一覧を[Expense]の[List]として取得する。
     * [start]および[end]と同日の出費も取得される。
     */
    fun findList(start: LocalDate, end: LocalDate): List<Expense> {
        return jdbcClient.sql("""
            SELECT
              id, 支払日, 費目cd, 金額, 支払先
            FROM
              出費履歴
            WHERE
              支払日 BETWEEN ? AND ?
        """.trimIndent())
            .params(start, end)
            .query(expenseMapper)
            .list()
    }

    /**
     * 出費情報（[expense]）を永続化し、登録されたidを[UUID]で返す
     */
    fun register(expense: Expense): UUID {
        return jdbcClient.sql("""
            INSERT INTO 出費履歴 (支払日, 金額, 支払先, 費目cd)
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

    /**
     * テーブル「出費履歴」の[ResultSet]を[Expense]にマッピングするための[RowMapper]
     * TODO Repositoryのprivateなプロパティとして持つのが正しいのか（何かそれ用にクラスやファイルを用意すべきでないか）は要検討
     * TODO カラム名がベタ書きなのも要改善点。テーブルの情報を管理するクラスを作るか？
     */
    private val expenseMapper = RowMapper { rs: ResultSet, _: Int ->
        Expense(
            id = UUID.fromString(rs.getString("id")),
            date = rs.getDate("支払日").toLocalDate(),
            price = rs.getInt("金額"),
            store = rs.getString("支払先"),
            type = rs.getInt("費目cd")
        )
    }

}
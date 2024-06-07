package com.thurofuji.expensesBook.repository

import com.thurofuji.expensesBook.model.Expense
import org.springframework.jdbc.core.RowMapper
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.simple.JdbcClient
import java.sql.ResultSet
import java.time.LocalDate
import java.util.UUID

/**
 * 家計簿に関する情報の永続化、および永続化された情報へのアクセスを行うRepositoryクラス
 */
@Repository
class ExpenseBookRepository(private val jdbcClient: JdbcClient) {
    // TODO JdbcClientを通してDBアクセス

    /**
     * [start]から[end]までの期間の出費一覧を[Expense]の[List]として取得する。
     * [start]および[end]と同日の出費も取得される。
     */
    fun findList(start: LocalDate, end: LocalDate): List<Expense> {
        return jdbcClient.sql("""
            SELECT
              id, 購入日, 費目cd, 金額, 店舗
            FROM
              出費
            WHERE
              購入日 BETWEEN ? AND ?
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

    /**
     * テーブル「出費」の[ResultSet]を[Expense]にマッピングするための[RowMapper]
     * TODO Repositoryのprivateなプロパティとして持つのが正しいのか（何かそれ用にクラスやファイルを用意すべきでないか）は要検討
     * TODO カラム名がベタ書きなのも要改善点。テーブルの情報を管理するクラスを作るか？
     */
    private val expenseMapper = RowMapper { rs: ResultSet, _: Int ->
        Expense(
            id = UUID.fromString(rs.getString("id")),
            date = rs.getDate("購入日").toLocalDate(),
            price = rs.getInt("金額"),
            store = rs.getString("店舗"),
            type = rs.getInt("費目cd")
        )
    }

}
package com.thurofuji.expensesBook.repository

import com.thurofuji.expensesBook.model.RequestedExpense
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.time.LocalDate
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

/**
 * 家計簿に関する情報の永続化、および永続化された情報へのアクセスを行うRepositoryクラス
 */
@Repository
class ExpenseBookRepository(private val jdbcClient: JdbcClient) {
    /**
     * [start]から[end]までの期間の出費一覧を[RequestedExpense]の[List]として取得する。
     * [start]および[end]と同日の出費も取得される。
     * [typeList]が空でない場合、費目での絞り込みも行う。
     */
    fun findList(start: LocalDate, end: LocalDate, typeList: List<Int>): List<RequestedExpense> {
        // TODO できれば条件に応じたSQLの構築をもっとスッキリさせたい（if文を使わないなど）。詳細は Issue #1 参照
        val sql = """
            SELECT
              id, 支払日, 費目cd, 金額, 支払先, 使途
            FROM
              出費履歴
            WHERE
              支払日 BETWEEN :start AND :end
        """ + if (typeList.isNotEmpty()) {
            " AND 費目cd in (:types)"
        } else { "" } + """
           ORDER BY 支払日, id
        """

        return jdbcClient.sql(sql.trimIndent())
            .param("start", start)
            .param("end", end)
            .param("types", typeList)
            .query(expenseMapper)
            .list()
    }

    /**
     * [id]で指定された出費を取得する。
     * 該当する出費が存在しなければ`null`を返す
     */
    fun findDetail(id: UUID): RequestedExpense? {
        val sql = """
            SELECT
              id, 支払日, 費目cd, 金額, 支払先, 使途
            FROM
              出費履歴
            WHERE
              id = :id
        """

        return jdbcClient.sql(sql.trimIndent())
            .param("id", id).query(expenseMapper)
            .optional().getOrNull()
    }

    /**
     * 出費情報（[expense]）を永続化し、登録された出費([RequestedExpense])を返す
     */
    fun register(expense: RequestedExpense): RequestedExpense {
        val registeredID: UUID = jdbcClient.sql("""
            INSERT INTO 出費履歴 (支払日, 金額, 支払先, 使途, 費目cd)
            VALUES (?, ?, ?, ?, ?)
            RETURNING id
        """.trimIndent())
            .params(
                expense.date, expense.price, expense.store, expense.usage, expense.type
            ).query(UUID::class.java)
            .single()

        return expense.copy(id = registeredID)
    }

    /**
     * 既存の出費情報（[RequestedExpense]）を更新し、更新された行数を返す
     */
    fun update(expense: RequestedExpense): Int {
        return jdbcClient.sql("""
            UPDATE
              出費履歴
            SET
              支払日 = ?
              , 費目cd = ?
              , 金額 = ?
              , 支払先 = ?
              , 使途 = ?
              , 最終更新日時 = CURRENT_TIMESTAMP
            WHERE
              id = ?
            """.trimIndent())
            .params(
                expense.date
                , expense.type
                , expense.price
                , expense.store
                , expense.usage
                , expense.id)
            .update()
    }

    /**
     * [id]で指定された出費情報を削除し、削除された行数を返す
     */
    fun delete(id: UUID): Int {
        return jdbcClient.sql("""
            DELETE FROM
              出費履歴
            WHERE
              id = ?
        """.trimIndent())
            .param(id)
            .update()
    }

    /**
     * テーブル「出費履歴」の[ResultSet]を[RequestedExpense]にマッピングするための[RowMapper]
     * TODO Repositoryのprivateなプロパティとして持つのが正しいのか（何かそれ用にクラスやファイルを用意すべきでないか）は要検討
     * TODO カラム名がベタ書きなのも要改善点。テーブルの情報を管理するクラスを作るか？
     */
    private val expenseMapper = RowMapper { rs: ResultSet, _: Int ->
        RequestedExpense(
            id = UUID.fromString(rs.getString("id")),
            date = rs.getDate("支払日").toLocalDate(),
            price = rs.getInt("金額"),
            store = rs.getString("支払先"),
            usage = rs.getString("使途"),
            type = rs.getInt("費目cd")
        )
    }

}
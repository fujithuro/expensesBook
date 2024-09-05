package com.thurofuji.expensesBook.repository

import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.dto.NewExpenseDto
import com.thurofuji.expensesBook.dxo.toDto
import com.thurofuji.expensesBook.entity.出費履歴
import com.thurofuji.expensesBook.entity.費目マスター
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
     * [start]から[end]までの期間の出費一覧を[出費履歴]の[List]として取得する。
     * [start]および[end]と同日の出費も取得される。
     * [typeList]が空でない場合、費目での絞り込みも行う。
     */
    fun findList(start: LocalDate, end: LocalDate, typeList: List<Int>): List<出費履歴> {
        // TODO できれば条件に応じたSQLの構築をもっとスッキリさせたい（if文を使わないなど）。詳細は Issue #1 参照
        val sql = """
            SELECT
              id, 支払日, 費目cd, 金額, 支払先, 使途, 最終更新者id
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
    fun findDetail(id: UUID): 出費履歴? {
        val sql = """
            SELECT
              id, 支払日, 費目cd, 金額, 支払先, 使途, 最終更新者id
            FROM
              出費履歴
            WHERE
              id = :id
        """

        return jdbcClient.sql(sql.trimIndent())
            .param("id", id)
            .query(expenseMapper)
            .optional().getOrNull()
    }

    /**
     * 出費情報（[expense]）を永続化し、登録された出費([ExpenseDto])を返す
     */
    fun register(expense: NewExpenseDto): ExpenseDto {
        val registeredID: UUID = jdbcClient.sql("""
            INSERT INTO 出費履歴 (支払日, 金額, 支払先, 使途, 費目cd, 最終更新者id)
            VALUES (:date, :price, :store, :usage, :type, :userId)
            RETURNING id
        """.trimIndent())
            .param("date", expense.支払日)
            .param("price", expense.金額)
            .param("store", expense.支払先)
            .param("usage", expense.使途)
            .param("type", expense.費目.費目cd)
            .param("userId", expense.登録者id)
            .query(UUID::class.java)
            .single()

        return expense.toDto(registeredID)
    }

    /**
     * 既存の出費情報（[expense]）を更新し、更新された行数を返す
     */
    fun update(expense: ExpenseDto): Int {
        return jdbcClient.sql("""
            UPDATE
              出費履歴
            SET
              支払日 = :date
              , 費目cd = :type
              , 金額 = :price
              , 支払先 = :store
              , 使途 = :usage
              , 最終更新者id = :userId
              , 最終更新日時 = CURRENT_TIMESTAMP
            WHERE
              id = :id
            """.trimIndent())
            .param("date", expense.支払日)
            .param("type", expense.費目.費目cd)
            .param("price", expense.金額)
            .param("store", expense.支払先)
            .param("usage", expense.使途)
            .param("userId", expense.最終更新者id)
            .param("id", expense.id)
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
              id = :id
        """.trimIndent())
            .param("id", id)
            .update()
    }

    /**
     * [費目マスター]の一覧を取得する
     */
    fun findExpenseTypeList(): List<費目マスター> {
        return jdbcClient.sql("""
            SELECT
              費目cd, 費目名, 有効区分
            FROM
              費目マスター
            ORDER BY 費目cd
        """.trimIndent())
            .query(expenseTypeMapper)
            .list()
    }

    /**
     * テーブル「出費履歴」の[ResultSet]を[出費履歴]にマッピングするための[RowMapper]
     */
    private val expenseMapper = RowMapper { rs: ResultSet, _: Int ->
        出費履歴(
            id = UUID.fromString(rs.getString(出費履歴.id))
            , 支払日 = rs.getDate(出費履歴.支払日).toLocalDate()
            , 金額 = rs.getInt(出費履歴.金額)
            , 支払先 = rs.getString(出費履歴.支払先)
            , 使途 = rs.getString(出費履歴.使途)
            , 費目cd = rs.getInt(出費履歴.費目cd)
            , 最終更新者id = rs.getInt(出費履歴.最終更新者id)
        )
    }

    /**
     * テーブル「費目マスター」の[ResultSet]を[費目マスター]にマッピングするための[RowMapper]
     */
    private val expenseTypeMapper: RowMapper<費目マスター> = RowMapper { rs: ResultSet, _: Int ->
        費目マスター(
            費目cd = rs.getInt(費目マスター.費目cd)
            , 費目名 = rs.getString(費目マスター.費目名)
            , 有効区分 = rs.getBoolean(費目マスター.有効区分)
        )
    }

}
package com.thurofuji.expensesBook.repository

import com.thurofuji.expensesBook.dto.ExpenseDto
import com.thurofuji.expensesBook.dto.ListSearchCondition
import com.thurofuji.expensesBook.dto.NewExpenseDto
import com.thurofuji.expensesBook.entity.出費履歴
import com.thurofuji.expensesBook.entity.費目マスター
import java.time.LocalDate
import java.util.UUID

/**
 * データソースから出費情報を取得したり、出費を編集したりするためのリポジトリインターフェース
 */
interface ExpenseBookRepository {

    /**
     * [condition]に合致する出費一覧を[出費履歴]の[List]として取得する。
     */
    fun findList(condition: ListSearchCondition): List<出費履歴>

    /**
     * [start]から[end]までの期間の出費一覧を[出費履歴]の[List]として取得する。
     * [start]および[end]と同日の出費も取得される。
     * [typeList]が空でない場合、費目での絞り込みも行う。
     */
    fun findList(start: LocalDate, end: LocalDate, typeList: List<Int>): List<出費履歴>

    /**
     * [id]で指定された出費を取得する。
     * 該当する出費が存在しなければ`null`を返す
     */
    fun findDetail(id: UUID): 出費履歴?

    /**
     * 出費情報（[expense]）を永続化し、登録された出費([ExpenseDto])を返す
     */
    fun register(expense: NewExpenseDto): ExpenseDto

    /**
     * 既存の出費情報（[expense]）を更新し、更新された行数を返す
     */
    fun update(expense: ExpenseDto): Int

    /**
     * [id]で指定された出費情報を削除し、削除された行数を返す
     */
    fun delete(id: UUID): Int

    /**
     * [費目マスター]の一覧を取得する
     */
    fun findExpenseTypeList(): List<費目マスター>

}

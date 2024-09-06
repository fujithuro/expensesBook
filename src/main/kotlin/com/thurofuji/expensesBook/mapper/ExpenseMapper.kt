package com.thurofuji.expensesBook.mapper

import com.thurofuji.expensesBook.dto.ListSearchCondition

/**
 * 出費に関する情報を扱うモデルを、別のモデルに変換するMapper
 */
interface ExpenseMapper {

    /**
     * 対象年月（[yyyyMM]）と費目（[types]）から[ListSearchCondition]を作成する
     */
    fun toSearchCondition(yyyyMM: String, types: List<Int>): ListSearchCondition

}
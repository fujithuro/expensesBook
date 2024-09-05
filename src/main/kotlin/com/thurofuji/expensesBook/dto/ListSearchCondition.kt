package com.thurofuji.expensesBook.dto

import java.time.LocalDate

/**
 * 出費の一覧を検索するための条件を表すモデル
 */
data class ListSearchCondition(val start: LocalDate
                               , val end: LocalDate
                               , val typeList: List<Int>)
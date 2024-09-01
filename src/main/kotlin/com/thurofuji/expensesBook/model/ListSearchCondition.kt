package com.thurofuji.expensesBook.model

import java.time.YearMonth

/**
 * 出費の一覧を検索するための条件を表すモデル
 */
data class ListSearchCondition(val targetYearMonth: YearMonth,
                               val typeList: List<ExpenseType>)
package com.thurofuji.expensesBook.exception

/**
 * 家計簿アプリケーションで発生した問題を扱うための独自例外
 */
sealed class ExpenseBookException(message: String, cause: Throwable? = null): RuntimeException(message)

/** 費目コードが不正な値だった場合の例外 */
class InvalidExpenseTypeException(message: String, cause: Throwable? = null): ExpenseBookException(message, cause)
/** 対象年月が不正な値だった場合の例外 */
class InvalidTargetYearMonthException(message: String, cause: Throwable? = null): ExpenseBookException(message, cause)

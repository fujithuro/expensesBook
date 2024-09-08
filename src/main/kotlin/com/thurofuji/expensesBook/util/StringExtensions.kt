package com.thurofuji.expensesBook.util

import com.thurofuji.expensesBook.exception.InvalidTargetYearMonthException
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * 家計簿アプリ全体で使用される、文字列関係の拡張関数を集めたファイル
 */

/**
 * 文字列をパースして[YearMonth]として返す
 *
 * @throws InvalidTargetYearMonthException 文字列が年月として扱えない場合にスローされる
 */
fun String.parseYearMonth(): YearMonth = runCatching {
    YearMonth.parse(this, DateTimeFormatter.ofPattern("yyyyMM"))
}.getOrElse { throw InvalidTargetYearMonthException("Invalid value for YearMonth.: $this", it) }

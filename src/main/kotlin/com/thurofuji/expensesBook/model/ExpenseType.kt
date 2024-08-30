package com.thurofuji.expensesBook.model

/**
 * 家計簿で扱う「費目」の列挙型
 */
enum class ExpenseType(val code: Int, private val displayName: String = "") {
    食費雑費_スーパーなど(1, "食費・雑費（スーパーなど）"),
    食費雑費_外食など(2, "食費・雑費（外食など）"),
    娯楽(3),
    身だしなみ(4),
    ガソリン(5),
    設備(6),
    学習(7),
    医療(8),
    その他(9);

    override fun toString(): String = displayName.ifBlank { name }
}
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

    companion object {
        /**
         * [value]が[ExpenseType.code]と合致する[ExpenseType]の項目を返す
         *
         * @throws IllegalArgumentException [value]と合致する項目が存在しない場合にスローされる
         */
        fun valueOf(value: Int): ExpenseType {
            return entries.firstOrNull { it.code == value }
                ?: throw IllegalArgumentException("Invalid value for expense code.: $value")
        }
    }

    override fun toString(): String = displayName.ifBlank { name }
}
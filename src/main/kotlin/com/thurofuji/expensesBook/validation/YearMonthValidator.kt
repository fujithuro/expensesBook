package com.thurofuji.expensesBook.validation

import com.thurofuji.expensesBook.annotation.ValidYearMonth
import com.thurofuji.expensesBook.util.parseYearMonth
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class YearMonthValidator : ConstraintValidator<ValidYearMonth, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true  // nullチェックは`@NotNull`で行われるので、ここでは対象外
        }

        return runCatching { value.parseYearMonth() }.isSuccess
    }
}

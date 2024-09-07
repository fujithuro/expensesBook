package com.thurofuji.expensesBook.validation

import com.thurofuji.expensesBook.annotation.ValidYearMonth
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class YearMonthValidator : ConstraintValidator<ValidYearMonth, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true  // nullチェックは`@NotNull`で行われるので、ここでは対象外
        }

        return runCatching {
            YearMonth.parse(value, DateTimeFormatter.ofPattern("yyyyMM"))
        }.isSuccess
    }
}

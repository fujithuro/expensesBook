package com.thurofuji.expensesBook.validation

import com.thurofuji.expensesBook.annotation.ValidExpenseType
import com.thurofuji.expensesBook.service.ExpenseTypeService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ExpenseTypeListValidator(private val expenseTypeService: ExpenseTypeService):
    ConstraintValidator<ValidExpenseType, List<Int>> {

    override fun isValid(value: List<Int>?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true // nullチェックは`@NotNull`で行われるので、ここでは対象外
        }
        return value.all { runCatching { expenseTypeService.getExpenseType(it) }.isSuccess }
    }

}

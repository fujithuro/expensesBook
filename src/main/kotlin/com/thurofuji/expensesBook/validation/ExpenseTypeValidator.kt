package com.thurofuji.expensesBook.validation

import com.thurofuji.expensesBook.annotation.ValidExpenseType
import com.thurofuji.expensesBook.exception.InvalidExpenseTypeException
import com.thurofuji.expensesBook.service.ExpenseTypeService
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component

@Component
class ExpenseTypeValidator(private val expenseTypeService: ExpenseTypeService):
    ConstraintValidator<ValidExpenseType, Int> {

    override fun isValid(value: Int?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true  // nullチェックは`@NotNull`で行われるので、ここでは対象外
        }

        return try {
            expenseTypeService.getExpenseType(value)
            true
        } catch (e: InvalidExpenseTypeException) {
            false
        }
    }
}
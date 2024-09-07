package com.thurofuji.expensesBook.annotation

import com.nimbusds.jose.Payload
import com.thurofuji.expensesBook.validation.ExpenseTypeListValidator
import com.thurofuji.expensesBook.validation.ExpenseTypeValidator
import jakarta.validation.Constraint
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ExpenseTypeValidator::class, ExpenseTypeListValidator::class])
annotation class ValidExpenseType(
    val message: String = "Invalid expense type.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

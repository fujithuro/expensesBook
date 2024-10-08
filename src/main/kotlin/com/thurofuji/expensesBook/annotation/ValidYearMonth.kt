package com.thurofuji.expensesBook.annotation

import com.thurofuji.expensesBook.validation.YearMonthValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [YearMonthValidator::class])
annotation class ValidYearMonth(
    val message: String = "Specify the year and month in `yyyyyMM`.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

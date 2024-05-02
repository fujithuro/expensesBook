package com.thurofuji.expensesBook

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExpensesBookApplication

fun main(args: Array<String>) {
	runApplication<ExpensesBookApplication>(*args)
}

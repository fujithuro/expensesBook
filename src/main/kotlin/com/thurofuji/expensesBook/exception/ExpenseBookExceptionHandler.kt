package com.thurofuji.expensesBook.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.format.DateTimeParseException

@RestControllerAdvice
class ExpenseBookExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(ExpenseBookExceptionHandler::class.java)


    /**
     * [MethodArgumentTypeMismatchException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Void> {
        logger.info("Input validation failed.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * [MethodArgumentNotValidException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Void> {
        logger.info("Input validation failed.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * [HttpMessageNotReadableException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Void> {
        logger.info("Input validation failed.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * [InvalidExpenseTypeException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(InvalidExpenseTypeException::class)
    fun handleInvalidExpenseTypeException(ex: InvalidExpenseTypeException): ResponseEntity<Void> {
        logger.info("Input validation failed.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * [DateTimeParseException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(DateTimeParseException::class)
    fun handleDateTimeParseException(ex: DateTimeParseException): ResponseEntity<Void> {
        logger.info("Input validation failed.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * 予期せぬ例外がスローされた場合のハンドリングを行う。
     * 例外の詳細を記録し、レスポンスとして`500 Internal Server Error`を返す
     */
    @Suppress("UNUSED")
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<Void> {
        logger.error("Unexpected exception occurred: {}", ex.message, ex)
        return ResponseEntity.internalServerError().build()
    }

}
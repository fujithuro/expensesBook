package com.thurofuji.expensesBook.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class ExpenseBookExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(ExpenseBookExceptionHandler::class.java)

    /**
     * [MethodArgumentTypeMismatchException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Void> {
        logger.info("Invalid type for method parameter.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * [MethodArgumentNotValidException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Void> {
        logger.info("Validation failed in request body.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * [HandlerMethodValidationException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleHandlerMethodValidationException(ex: HandlerMethodValidationException): ResponseEntity<Void> {
        logger.info("Validation failed in method argument.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * [HttpMessageNotReadableException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Void> {
        logger.info("Unable to read HTTP message.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * リクエストされた費目が不正な場合にスローされる[InvalidExpenseTypeException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(InvalidExpenseTypeException::class)
    fun handleInvalidExpenseTypeException(ex: InvalidExpenseTypeException): ResponseEntity<Void> {
        logger.info("Invalid expense type.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * リクエストされた対象年月が不正な場合にスローされる[InvalidTargetYearMonthException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(InvalidTargetYearMonthException::class)
    fun handleInvalidTargetYearMonthException(ex: InvalidTargetYearMonthException): ResponseEntity<Void> {
        logger.info("Failed to parse YearMonth: {}", ex.message)
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
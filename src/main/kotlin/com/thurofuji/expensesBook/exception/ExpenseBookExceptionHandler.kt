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
     * Springでの入力値検証でスローされる[MethodArgumentTypeMismatchException]のハンドリングを行う。
     * リクエストパラメータが、エンドポイントのメソッドや関数で期待されている型と一致しない場合にスローされる。
     */
    @Suppress("UNUSED")
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<Void> {
        logger.info("Argument type mismatch.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * Springでの入力値検証でスローされる[MethodArgumentNotValidException]のハンドリングを行う。
     * `@Valid`や`@Validated`が付与されたオブジェクトのプロパティでバリデーションに失敗した際にスローされる。
     */
    @Suppress("UNUSED")
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<Void> {
        logger.info("Validation failed for request body parameters.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * Springでの入力値検証でスローされる[HandlerMethodValidationException]のハンドリングを行う。
     * Controllerが持つメソッドのパラメータや戻り値に対する、メソッドレベルでのバリデーションに失敗した場合にスローされる。
     */
    @Suppress("UNUSED")
    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleHandlerMethodValidationException(ex: HandlerMethodValidationException): ResponseEntity<Void> {
        logger.info("Validation failed for controller method.: {}", ex.message)
        return ResponseEntity.badRequest().build()
    }

    /**
     * Springでの入力値検証でスローされる[HttpMessageNotReadableException]のハンドリングを行う。
     * リクエストボディのJSONやXMLなどの形式に問題があり、適切にパースできない場合にスローされる。
     */
    @Suppress("UNUSED")
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<Void> {
        logger.info("Failed to parse HTTP request body.: {}", ex.message)
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
        logger.info("Failed to parse YearMonth.: {}", ex.message)
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
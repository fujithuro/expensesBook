package com.thurofuji.expensesBook.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.HandlerMethodValidationException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.LocalDateTime

@RestControllerAdvice
class ExpenseBookExceptionHandler {

    private val logger: Logger = LoggerFactory.getLogger(ExpenseBookExceptionHandler::class.java)

    /**
     * Springでの入力値検証でスローされる[MethodArgumentTypeMismatchException]のハンドリングを行う。
     * リクエストパラメータが、エンドポイントのメソッドや関数で期待されている型と一致しない場合にスローされる。
     */
    @Suppress("UNUSED")
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponseBody> {
        val errorMessage = "Invalid value for parameter '${ex.name}'. Expected ${ex.requiredType?.simpleName}, but got '${ex.value}'."
        val errorResponse = ErrorResponseBody(
            error = "Argument Type Mismatch",
            message = errorMessage,
            details = listOf(errorMessage)
        )
        logger.info("Argument type mismatch: {}", errorMessage)
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Springでの入力値検証でスローされる[MethodArgumentNotValidException]のハンドリングを行う。
     * `@Valid`や`@Validated`が付与されたオブジェクトのプロパティでバリデーションに失敗した際にスローされる。
     */
    @Suppress("UNUSED")
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponseBody> {
        val errors = ex.bindingResult.fieldErrors.map { error ->
            "${error.field}: ${error.defaultMessage}"
        }
        val errorResponse = ErrorResponseBody(
            error = "Validation Failed",
            message = "Invalid input data",
            details = errors
        )
        logger.info("Validation failed: {}", errors)
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Springでの入力値検証でスローされる[HandlerMethodValidationException]のハンドリングを行う。
     * Controllerが持つメソッドのパラメータや戻り値に対する、メソッドレベルでのバリデーションに失敗した場合にスローされる。
     */
    @Suppress("UNUSED")
    @ExceptionHandler(HandlerMethodValidationException::class)
    fun handleHandlerMethodValidationException(ex: HandlerMethodValidationException): ResponseEntity<ErrorResponseBody> {
        val errors = ex.allValidationResults.flatMap { result ->
            result.resolvableErrors.map { error ->
                "${result.argument}: ${error.defaultMessage}"
            }
        }
        val errorResponse = ErrorResponseBody(
            error = "Method Validation Failed",
            message = "Invalid method parameters or return value",
            details = errors
        )
        logger.info("Method Validation Failed: {}", errors)
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Springでの入力値検証でスローされる[HttpMessageNotReadableException]のハンドリングを行う。
     * リクエストボディのJSONやXMLなどの形式に問題があり、適切にパースできない場合にスローされる。
     */
    @Suppress("UNUSED")
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponseBody> {
        // HttpMessageNotReadableExceptionから取得できる情報はソースコード内部の情報を含む場合がある
        // セキュリティリスクを抑えるため、例外からの情報はユーザーには返さず、リクエストを処理できなかったことだけ伝える
        val errorResponse = ErrorResponseBody(
            error = "Invalid Request",
            message = "Failed to process the request. Please ensure all required fields are correctly formatted and try again.",
            details = emptyList()
        )
        logger.info("Failed to parse HTTP request body: {}", ex.mostSpecificCause.message)
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * リクエストされた費目が不正な場合にスローされる[InvalidExpenseTypeException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(InvalidExpenseTypeException::class)
    fun handleInvalidExpenseTypeException(ex: InvalidExpenseTypeException): ResponseEntity<ErrorResponseBody> {
        val errorResponse = ErrorResponseBody(
            error = "Invalid Expense Type",
            message = ex.message ?: "Invalid expense type provided",
            details = emptyList()
        )
        logger.info("Invalid expense type: {}", ex.message)
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * リクエストされた対象年月が不正な場合にスローされる[InvalidTargetYearMonthException]のハンドリングを行う
     */
    @Suppress("UNUSED")
    @ExceptionHandler(InvalidTargetYearMonthException::class)
    fun handleInvalidTargetYearMonthException(ex: InvalidTargetYearMonthException): ResponseEntity<ErrorResponseBody> {
        val errorResponse = ErrorResponseBody(
            error = "Invalid Target Year Month",
            message = ex.message ?: "Invalid target year month provided",
            details = emptyList()
        )
        logger.info("Failed to parse YearMonth: {}", ex.message)
        return ResponseEntity.badRequest().body(errorResponse)
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

    /**
     * エラーのレスポンスボディを表すモデル
     */
    data class ErrorResponseBody(
        val timestamp: LocalDateTime = LocalDateTime.now(),
        val status: String = HttpStatus.BAD_REQUEST.toString(),
        val error: String,
        val message: String,
        val details: List<String>
    )

}

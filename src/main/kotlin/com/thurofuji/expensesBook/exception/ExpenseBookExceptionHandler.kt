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
     * 入力値検証で例外がスローされた場合のハンドリングを行う。
     * この例外ハンドラでは主に、「入力値の型やフォーマットが一致せず、引数に値を設定できなかった」など引数の問題で例外がスローされた場合のハンドリングを担当している。
     *
     * [MethodArgumentTypeMismatchException]: 引数に対して型が不一致な場合にスローされる
     * [MethodArgumentNotValidException]: メソッドの引数に対する入力値検証で問題が見つかった場合にスローされる
     * [HttpMessageNotReadableException]: リクエスト内容をオブジェクトに展開できなかった場合にスローされる。たとえばnull非許容のプロパティにnullが送信された、型が合致しないなど
     * [InvalidExpenseTypeException]: リクエストされた費目に問題があった場合にスローされる
     * [DateTimeParseException]: 年月日のパースに失敗した場合にスローされる
     */
    @Suppress("UNUSED")
    @ExceptionHandler(
        MethodArgumentTypeMismatchException::class
        , MethodArgumentNotValidException::class
        , HttpMessageNotReadableException::class
        , InvalidExpenseTypeException::class
        , DateTimeParseException::class
    )
    fun handleValidationException(ex: Exception): ResponseEntity<Void> {
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
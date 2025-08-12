package org.deblock.org.deblock.app.controller

import mu.KotlinLogging
import org.deblock.org.deblock.app.response.ErrorResponse
import org.deblock.org.deblock.app.response.ErrorResponse.Companion.badRequestResponse
import org.deblock.org.deblock.app.response.ErrorResponse.Companion.genericErrorResponse
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ExceptionController {

    private val logger = KotlinLogging.logger { }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun defaultExceptionHandler(exception: Exception): ErrorResponse {
        logger.error(exception) { "Exception occurred while processing request" }
        return genericErrorResponse()
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(BAD_REQUEST)
    fun invalidRequestParamsHandler(exception: MethodArgumentNotValidException): ErrorResponse {
        logger.error(exception) { "Bad request while processing request" }
        return badRequestResponse(exception.gatherErrors())
    }

    private fun MethodArgumentNotValidException.gatherErrors() = fieldErrors.map {
        "Field ${it.field} ${it.defaultMessage ?: ""}, rejected value: ${it.rejectedValue}"
    }
}
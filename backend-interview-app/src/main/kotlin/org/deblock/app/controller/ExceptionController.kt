package org.deblock.org.deblock.app.controller

import mu.KotlinLogging
import org.deblock.org.deblock.app.response.ErrorResponse
import org.deblock.org.deblock.app.response.ErrorResponse.Companion.genericErrorResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class ExceptionController {

    private val logger = KotlinLogging.logger { }

    @ExceptionHandler
    @ResponseBody
    fun defaultExceptionHandler(exception: Exception): ErrorResponse {
        logger.error { "Exception occurred while processing request: $exception" }
        return genericErrorResponse()
    }
}
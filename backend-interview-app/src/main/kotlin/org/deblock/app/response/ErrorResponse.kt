package org.deblock.org.deblock.app.response

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

data class ErrorResponse(
    val message: String,
    val errorCode: Int,
    val errors: List<String> = emptyList(),
) {

    companion object {
        fun genericErrorResponse() = ErrorResponse(
            message = "Internal error occurred. Please contact development team.",
            errorCode = INTERNAL_SERVER_ERROR.value(),
        )

        fun badRequestResponse(errors: List<String>) = ErrorResponse(
            message = "Error occurred while validating request parameters",
            errors = errors,
            errorCode = BAD_REQUEST.value(),
        )
    }
}
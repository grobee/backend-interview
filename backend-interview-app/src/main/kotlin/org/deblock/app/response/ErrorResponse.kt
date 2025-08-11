package org.deblock.org.deblock.app.response

import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

data class ErrorResponse(
    val message: String,
    val errorCode: Int,
) {

    companion object {
        fun genericErrorResponse() = ErrorResponse(
            message = "Internal error occurred. Please contact development team.",
            errorCode = INTERNAL_SERVER_ERROR.value(),
        )
    }
}
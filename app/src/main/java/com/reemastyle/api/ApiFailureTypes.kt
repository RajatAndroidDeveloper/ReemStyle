package com.reemastyle.api

import com.reemastyle.util.Constants.FAILURE_INTERNET_CONNECTION
import com.reemastyle.util.Constants.FAILURE_SERVER_NOT_RESPONDING
import com.reemastyle.util.Constants.FAILURE_SOMETHING_WENT_WRONG
import com.reemastyle.util.Constants.FAILURE_TIME_OUT_ERROR


class ApiFailureTypes {
    fun getFailureMessage(error: Throwable?): String {
        val message: String
        when {
            error == null -> {
                message = FAILURE_TIME_OUT_ERROR
            }
            error.localizedMessage == null -> {
                message = FAILURE_TIME_OUT_ERROR
            }
            error.localizedMessage.toUpperCase().contains("ETIMEDOUT") -> {
                message = FAILURE_INTERNET_CONNECTION
            }
            error.localizedMessage.toUpperCase().contains("ECONNRESET") -> {
                message = FAILURE_INTERNET_CONNECTION
            }
            error.localizedMessage.toUpperCase().contains("FAILED TO CONNECT TO") -> {
                message = FAILURE_SERVER_NOT_RESPONDING
            }
            else -> {
                message = FAILURE_SOMETHING_WENT_WRONG
            }
        }
        return message
    }
}
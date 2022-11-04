package com.reemastyle.api

import com.google.gson.annotations.SerializedName
import com.reemastyle.api.Errors

data class ErrorResponse(
    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("errors")
    val errors: Errors? = null
)
package com.reemastyle.model.cart

import com.google.gson.annotations.SerializedName

data class AddToCartResponse(
	@field:SerializedName("cartID")
	val cartID: List<String?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

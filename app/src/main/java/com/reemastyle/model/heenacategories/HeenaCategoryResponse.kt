package com.reemastyle.model.heenacategories

import com.google.gson.annotations.SerializedName

data class HeenaCategoryResponse(

	@field:SerializedName("result")
	val result: ArrayList<ResultItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ResultItem(

	@field:SerializedName("IMG")
	val iMG: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

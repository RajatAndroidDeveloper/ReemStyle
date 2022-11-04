package com.reemastyle.model.zones

import com.google.gson.annotations.SerializedName

data class ZonesResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("zones")
	val zones: List<ZonesItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ZonesItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("fee")
	val fee: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
){
	override fun toString(): String {
		return name?:""
	}
}


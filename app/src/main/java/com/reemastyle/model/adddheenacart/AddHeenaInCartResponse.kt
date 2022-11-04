package com.reemastyle.model.adddheenacart

import com.google.gson.annotations.SerializedName

data class AddHeenaInCartResponse(

	@field:SerializedName("addresslist")
	val addresslist: Addresslist? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Addresslist(

	@field:SerializedName("instructions")
	val instructions: String? = null,

	@field:SerializedName("latitude")
	val latitude: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null,

	@field:SerializedName("building")
	val building: String? = null,

	@field:SerializedName("zone")
	val zone: String? = null,

	@field:SerializedName("street")
	val street: String? = null,

	@field:SerializedName("flat")
	val flat: String? = null,

	@field:SerializedName("update_at")
	val updateAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("landline")
	val landline: String? = null,

	@field:SerializedName("floor")
	val floor: String? = null,

	@field:SerializedName("longitude")
	val longitude: String? = null
)

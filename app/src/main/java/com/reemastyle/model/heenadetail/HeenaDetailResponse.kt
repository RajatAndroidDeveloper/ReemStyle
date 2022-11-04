package com.reemastyle.model.heenadetail

import com.google.gson.annotations.SerializedName

data class HeenaDetailResponse(
    @field:SerializedName("slots")
    val slots: List<SlotsItem?>? = null,

    @field:SerializedName("heena_section")
    val heenaSection: List<HeenaSectionItem?>? = null,

    @field:SerializedName("status")
    val status: Boolean? = null,

    @field:SerializedName("useraddress")
    val useraddress: Useraddress? = null,

    @field:SerializedName("storeaddress")
    val storeaddress: Storeaddress? = null,
)

data class HeenaSectionItem(

    @field:SerializedName("heena_catID")
    val heenaCatID: String? = null,

    @field:SerializedName("heena_catname")
    val heenaCatname: String? = null,

    @field:SerializedName("heena_catIMG")
    val heenaCatIMG: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("optionname")
    val optionname: String? = null,

    @field:SerializedName("id")
    val id: String? = null
)


data class SlotsItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Storeaddress(

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

data class Useraddress(

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

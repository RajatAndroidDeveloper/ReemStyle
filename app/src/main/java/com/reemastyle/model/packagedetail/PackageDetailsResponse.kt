package com.reemastyle.model.packagedetail

import com.google.gson.annotations.SerializedName

data class PackageDetailsResponse(

	@field:SerializedName("slots")
	val slots: List<SlotsItem?>? = null,

	@field:SerializedName("packagedata")
	val packagedata: Packagedata? = null,

	@field:SerializedName("useraddress")
	val useraddress: Useraddress? = null,

	@field:SerializedName("storeaddress")
	val storeaddress: Storeaddress? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
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

data class SlotsItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Packservice(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("catID")
	val catID: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("discription")
	val discription: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("subcatID")
	val subcatID: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Packagedata(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("packname")
	val packname: String? = null,

	@field:SerializedName("discription")
	val discription: String? = null,

	@field:SerializedName("oldprice")
	val oldprice: String? = null,

	@field:SerializedName("packservice")
	val packservice: Packservice? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("discount")
	val discount: String? = null,

	@field:SerializedName("offertitle")
	val offertitle: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("status")
	val status: String? = null
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

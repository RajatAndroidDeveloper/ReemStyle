package com.reemastyle.model.packages

import com.google.gson.annotations.SerializedName

data class PackagesResponse(

	@field:SerializedName("packages")
	val packages: List<PackagesItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PackagesItem(

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

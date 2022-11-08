package com.reemastyle.model.subcategory

import com.google.gson.annotations.SerializedName

data class SubCategoriesResponse(

	@field:SerializedName("packages")
	val packages: List<PackagesItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Category(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("cat_name")
	val catName: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class PackagesItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("cat_name")
	val catName: String? = null,

	@field:SerializedName("servicecount")
	val servicecount: Int? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("category")
	val category: Category? = null,

	@field:SerializedName("priority")
	val priority: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

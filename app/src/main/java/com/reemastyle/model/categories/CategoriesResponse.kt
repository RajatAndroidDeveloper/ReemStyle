package com.reemastyle.model.categories

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
	@field:SerializedName("all_categories")
	val allCategories: List<AllCategoriesItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AllCategoriesItem(

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

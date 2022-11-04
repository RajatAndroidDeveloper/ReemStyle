package com.reemastyle.model.search

import com.google.gson.annotations.SerializedName

data class SearchCategoryResponse(

	@field:SerializedName("search_data")
	val searchData: List<SearchDataItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class SearchDataItem(

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

package com.reemastyle.model.gallery

import com.google.gson.annotations.SerializedName

data class GalleryResponse(

	@field:SerializedName("gallery")
	val gallery: List<GalleryItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class GalleryItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

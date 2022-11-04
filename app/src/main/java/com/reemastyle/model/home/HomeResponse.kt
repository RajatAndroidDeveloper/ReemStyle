package com.reemastyle.model.home

import alirezat775.lib.carouselview.CarouselModel
import com.google.gson.annotations.SerializedName

data class HomeResponse(

	@field:SerializedName("offers")
	val offers: List<OffersItem?>? = null,

	@field:SerializedName("testimonials")
	val testimonials: List<TestimonialsItem?>? = null,

	@field:SerializedName("categories")
	val categories: List<CategoriesItem?>? = null,

	@field:SerializedName("packages")
	val packages: List<PackagesItem?>? = null,

	@field:SerializedName("gallery")
	val gallery: List<GalleryItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class TestimonialsItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("review")
	val review: String? = null,

	@field:SerializedName("clientname")
	val clientname: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class CategoriesItem(

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("cat_name")
	val catName: String? = null,

	@field:SerializedName("id")
	val id: String? = null
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

data class OffersItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("offer_value")
	val offerValue: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("offer_type")
	val offerType: String? = null
)

data class GalleryItem(
	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("image")
	val image: String? = null,

	@field:SerializedName("id")
	val id: String? = null
): CarouselModel()

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

package com.reemastyle.model.history

import com.google.gson.annotations.SerializedName

data class OrderHistoryResponse(

	@field:SerializedName("cartids")
	val cartids: List<String?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("items")
	val items: List<ItemsItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ItemsItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("category_name")
	val categoryName: String? = null,

	@field:SerializedName("order_slot_id")
	var orderSlotId: String? = null,

	@field:SerializedName("cartID")
	val cartID: String? = null,

	@field:SerializedName("homeservice")
    var homeservice: String? = null,

	@field:SerializedName("order_slot_time")
	val orderSlotTime: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null,

	@field:SerializedName("order_date")
	var orderDate: String? = null,

	@field:SerializedName("totalAmount")
    var totalAmount: Double? = null,

	@field:SerializedName("category_id")
	val categoryId: String? = null,

	@field:SerializedName("subtotal")
    var subtotal: String? = null,

	@field:SerializedName("qty")
	var qty: String? = null,

	@field:SerializedName("options")
	val options: List<OptionsItem?>? = null,

	@field:SerializedName("addresstype")
	var addresstype: String? = null,

	@field:SerializedName("cartType")
	val cartType: String? = null,

	@field:SerializedName("service_price")
    var servicePrice: String? = null,

	@field:SerializedName("itemstatus")
	val itemstatus: String? = null,

	@field:SerializedName("subcategory_name")
	val subcategoryName: String? = null,

	@field:SerializedName("servicesID")
	val servicesID: String? = null,

	@field:SerializedName("subcategory_id")
	val subcategoryId: String? = null,

	@field:SerializedName("service_qty")
	var serviceQty: String? = null,

	@field:SerializedName("servicename")
	val servicename: String? = null
)

data class OptionsItem(

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("optionname")
	val optionname: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

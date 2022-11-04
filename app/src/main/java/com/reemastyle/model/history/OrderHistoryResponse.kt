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
	val orderSlotId: String? = null,

	@field:SerializedName("cartID")
	val cartID: String? = null,

	@field:SerializedName("homeservice")
	val homeservice: String? = null,

	@field:SerializedName("order_slot_time")
	val orderSlotTime: String? = null,

	@field:SerializedName("userid")
	val userid: String? = null,

	@field:SerializedName("order_date")
	val orderDate: String? = null,

	@field:SerializedName("totalAmount")
	val totalAmount: Int? = null,

	@field:SerializedName("category_id")
	val categoryId: String? = null,

	@field:SerializedName("subtotal")
    var subtotal: String? = null,

	@field:SerializedName("qty")
	val qty: String? = null,

	@field:SerializedName("options")
	val options: List<OptionsItem?>? = null,

	@field:SerializedName("addresstype")
	val addresstype: String? = null,

	@field:SerializedName("cartType")
	val cartType: String? = null,

	@field:SerializedName("service_price")
	val servicePrice: String? = null,

	@field:SerializedName("itemstatus")
	val itemstatus: String? = null,

	@field:SerializedName("subcategory_name")
	val subcategoryName: String? = null,

	@field:SerializedName("servicesID")
	val servicesID: String? = null,

	@field:SerializedName("subcategory_id")
	val subcategoryId: String? = null,

	@field:SerializedName("service_qty")
	val serviceQty: String? = null,

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

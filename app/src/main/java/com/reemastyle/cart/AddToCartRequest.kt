package com.reemastyle.cart

import com.reemastyle.util.Utils

class AddToCartRequest() {
    var action = "AddtoCart"
    var userid = Utils.getUserData()?.id
    var categoryId = "0"
    var subcategory_id = "0"
    var order_slot_id = "0"
    var order_slot_time = "0"
    var addresstype = "home"
    var addressId = "50"
    var totalAmount = "0"
    var services: ArrayList<ServiceModel> = ArrayList<ServiceModel>()
    var order_date = ""
}

data class ServiceModel(var id: String, var quantity: String,var price: Double)
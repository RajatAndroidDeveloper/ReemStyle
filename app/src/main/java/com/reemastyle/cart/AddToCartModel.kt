package com.reemastyle.cart

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.address.AddressRepository
import com.reemastyle.api.MyViewModel
import com.reemastyle.model.address.AddAddressResponse
import com.reemastyle.model.cart.AddToCartResponse
import com.reemastyle.model.cart.SavedCartResponse
import com.reemastyle.model.slots.TimeSlotResponse

class AddToCartModel: MyViewModel() {
    var addToCartResponse = MutableLiveData<AddToCartResponse>()
    var savedCartResponse = MutableLiveData<SavedCartResponse>()
    var deleteCartResponse = MutableLiveData<SavedCartResponse>()
    var updateCartResponse = MutableLiveData<SavedCartResponse>()
    var placeOrderResponse = MutableLiveData<SavedCartResponse>()
    var timeSlotsResponse = MutableLiveData<TimeSlotResponse>()

    fun addToCart(map: JsonObject) {
        isLoading.value = true
        AddToCartRepository.addToCart({
            isLoading.value = false
            addToCartResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getCartData(map: JsonObject) {
        isLoading.value = true
        AddToCartRepository.getCartData({
            isLoading.value = false
            savedCartResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun deleteCartItem(map: JsonObject) {
        isLoading.value = true
        AddToCartRepository.deleteCartItem({
            isLoading.value = false
            deleteCartResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun updateCartItem(map: JsonObject) {
        isLoading.value = true
        AddToCartRepository.updateCartItem({
            isLoading.value = false
            updateCartResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun updateHeenaItem(map: JsonObject) {
        isLoading.value = true
        AddToCartRepository.updateCartItem({
            isLoading.value = false
            updateCartResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun placeOrder(map: JsonObject) {
        isLoading.value = true
        AddToCartRepository.placeOrder({
            isLoading.value = false
            placeOrderResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getAllTimeSlots(map: JsonObject) {
        isLoading.value = true
        AddToCartRepository.getAllTimeSlots({
            isLoading.value = false
            timeSlotsResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }
}
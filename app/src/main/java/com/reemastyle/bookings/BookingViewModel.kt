package com.reemastyle.bookings

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.api.MyViewModel
import com.reemastyle.cart.AddToCartRepository
import com.reemastyle.model.cart.AddToCartResponse
import com.reemastyle.model.history.OrderHistoryResponse

class BookingViewModel: MyViewModel() {
    var bookingHistoryResponse = MutableLiveData<OrderHistoryResponse>()
    var bookingCancelResponse = MutableLiveData<OrderHistoryResponse>()

    fun getAllBookings(map: JsonObject) {
        isLoading.value = true
        BookingRepository.getAllBookings({
            isLoading.value = false
            bookingHistoryResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }
    fun cancelBooking(map: JsonObject) {
        isLoading.value = true
        BookingRepository.cancelBooking({
            isLoading.value = false
            bookingCancelResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

}
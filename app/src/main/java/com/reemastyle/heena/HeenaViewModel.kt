package com.reemastyle.heena

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.api.MyViewModel
import com.reemastyle.model.adddheenacart.AddHeenaInCartResponse

class HeenaViewModel: MyViewModel() {
    var addHeenaToCartResponse = MutableLiveData<AddHeenaInCartResponse>()

    fun addHeenaToCart(map: JsonObject) {
        isLoading.value = true
        HeenaRepository.addHeenaToCart({
            isLoading.value = false
            addHeenaToCartResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }
}
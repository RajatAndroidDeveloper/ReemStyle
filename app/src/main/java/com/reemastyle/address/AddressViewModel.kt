package com.reemastyle.address

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.api.MyViewModel
import com.reemastyle.model.address.AddAddressResponse
import com.reemastyle.model.categories.CategoriesResponse
import com.reemastyle.model.getaddress.GetAddressResponse
import com.reemastyle.model.zones.ZonesResponse

class AddressViewModel: MyViewModel() {
    var addressResponse = MutableLiveData<AddAddressResponse>()
    var getAddressResponse = MutableLiveData<GetAddressResponse>()
    var zonesResponse = MutableLiveData<ZonesResponse>()

    fun addAddress(map: JsonObject) {
        isLoading.value = true
        AddressRepository.addAddress({
            isLoading.value = false
            addressResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getAllAddress(map: JsonObject) {
        isLoading.value = true
        AddressRepository.getAllAddress({
            isLoading.value = false
            getAddressResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getAllZones(map: JsonObject) {
        isLoading.value = true
        AddressRepository.getAllZones({
            isLoading.value = false
            zonesResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }
}
package com.reemastyle.home

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.address.AddressRepository
import com.reemastyle.api.MyViewModel
import com.reemastyle.auth.AuthRepository
import com.reemastyle.model.address.AddAddressResponse
import com.reemastyle.model.getaddress.GetAddressResponse
import com.reemastyle.model.home.HomeResponse
import com.reemastyle.model.login.LoginResponse
import com.reemastyle.model.search.SearchCategoryResponse

class HomeViewModel: MyViewModel() {
    var homeResponse = MutableLiveData<HomeResponse>()
    var getAddressResponse = MutableLiveData<GetAddressResponse>()
    var searchCategoryResponse = MutableLiveData<SearchCategoryResponse>()
    var notificationsResponse = MutableLiveData<SearchCategoryResponse>()

    fun getHomeData(map: JsonObject) {
        isLoading.value = true
        HomeRepository.getHomeData({
            isLoading.value = false
            homeResponse.value = it
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

    fun searchCategory(map: JsonObject) {
        isLoading.value = true
        AddressRepository.searchcategory({
            isLoading.value = false
            searchCategoryResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getNotifications(map: JsonObject) {
        isLoading.value = true
        AddressRepository.getNotifications({
            isLoading.value = false
            notificationsResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }
}
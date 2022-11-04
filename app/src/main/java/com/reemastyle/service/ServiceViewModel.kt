package com.reemastyle.service

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.api.MyViewModel
import com.reemastyle.model.categories.CategoriesResponse
import com.reemastyle.model.heenacategories.HeenaCategoryResponse
import com.reemastyle.model.heenadetail.HeenaDetailResponse
import com.reemastyle.model.services.ServicesResponse
import com.reemastyle.model.subcategory.SubCategoriesResponse

class ServiceViewModel: MyViewModel() {
    var categoriesResponse = MutableLiveData<CategoriesResponse>()
    var subCategoriesResponse = MutableLiveData<SubCategoriesResponse>()
    var heenaCategoriesResponse = MutableLiveData<HeenaCategoryResponse>()
    var heenaCategoryDetailsResponse = MutableLiveData<HeenaDetailResponse>()
    var servicesResponse = MutableLiveData<ServicesResponse>()

    fun getCategories(map: JsonObject) {
        isLoading.value = true
        ServiceRepository.getCategories({
            isLoading.value = false
            categoriesResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getSubCategories(map: JsonObject) {
        isLoading.value = true
        ServiceRepository.getSubCategories({
            isLoading.value = false
            subCategoriesResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getHeenaCategories(map: JsonObject) {
        isLoading.value = true
        ServiceRepository.getHeenaCategories({
            isLoading.value = false
            heenaCategoriesResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getHeenaDetails(map: JsonObject) {
        isLoading.value = true
        ServiceRepository.getHeenaDetails({
            isLoading.value = false
            heenaCategoryDetailsResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getServices(map: JsonObject) {
        isLoading.value = true
        ServiceRepository.getServices({
            isLoading.value = false
            servicesResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }
}
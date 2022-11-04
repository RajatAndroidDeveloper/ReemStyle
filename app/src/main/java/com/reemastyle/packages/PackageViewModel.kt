package com.reemastyle.packages

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.api.MyViewModel
import com.reemastyle.model.packagedetail.PackageDetailsResponse
import com.reemastyle.model.packages.PackagesResponse

class PackageViewModel: MyViewModel() {
    var packageResponse = MutableLiveData<PackagesResponse>()
    var packageDetailsResponse = MutableLiveData<PackageDetailsResponse>()

    fun getPackageData(map: JsonObject) {
        isLoading.value = true
        PackageRepository.getPackageData({
            isLoading.value = false
            packageResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun getPackageDetail(map: JsonObject) {
        isLoading.value = true
        PackageRepository.getPackageDetails({
            isLoading.value = false
            packageDetailsResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }
}
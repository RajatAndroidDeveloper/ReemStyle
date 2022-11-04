package com.reemastyle.profile

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.api.MyViewModel
import com.reemastyle.model.profile.ProfileResponse
import okhttp3.RequestBody

class ProfileViewModel: MyViewModel() {
    var profileResponse = MutableLiveData<ProfileResponse>()
    var updateProfileResponse = MutableLiveData<ProfileResponse>()
    var changePasswordResponse = MutableLiveData<ProfileResponse>()

    fun getProfile(map: JsonObject) {
        isLoading.value = true
        ProfileRepository.getProfile({
            isLoading.value = false
            profileResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun changePassword(map: JsonObject) {
        isLoading.value = true
        ProfileRepository.changePassword({
            isLoading.value = false
            changePasswordResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun updateProfile(image: RequestBody, name: RequestBody, email: RequestBody, countryCode: RequestBody, mobile: RequestBody, id: RequestBody, action: RequestBody) {
        isLoading.value = true
        ProfileRepository.updateProfile({
            isLoading.value = false
            updateProfileResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, image,name,email,countryCode,mobile,id,action)
    }
}
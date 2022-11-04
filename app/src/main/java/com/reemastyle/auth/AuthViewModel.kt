package com.reemastyle.auth

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.api.MyViewModel
import com.reemastyle.model.login.LoginResponse

class AuthViewModel: MyViewModel() {
    var registerResponse = MutableLiveData<LoginResponse>()
    var loginResponse = MutableLiveData<LoginResponse>()
    var verifyOtpResponse = MutableLiveData<LoginResponse>()
    var resendOtpResponse = MutableLiveData<LoginResponse>()
    var forgotPasswordRequestResponse = MutableLiveData<LoginResponse>()

    fun registerUser(map: JsonObject) {
        isLoading.value = true
        AuthRepository.registerUser({
            isLoading.value = false
            registerResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun loginUser(map: JsonObject) {
        isLoading.value = true
        AuthRepository.loginUser({
            isLoading.value = false
            loginResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun verifyOtp(map: JsonObject) {
        isLoading.value = true
        AuthRepository.verifyOtp({
            isLoading.value = false
            verifyOtpResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun resendOtp(map: JsonObject) {
        isLoading.value = true
        AuthRepository.resendOtp({
            isLoading.value = false
            resendOtpResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }

    fun forgotPasswordEmailRequest(map: JsonObject) {
        isLoading.value = true
        AuthRepository.forgotPasswordEmailRequest({
            isLoading.value = false
            forgotPasswordRequestResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }
}
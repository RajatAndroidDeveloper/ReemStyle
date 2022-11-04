package com.reemastyle.api

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MyViewModel: ViewModel() {
    var apiError = MutableLiveData<String>()
    var showToast = MutableLiveData<String>()
    var onFailure = MutableLiveData<Throwable>()
    var badRequest = MutableLiveData<String>()
    var isLoading = MutableLiveData<Boolean>()
    var unauthorized = MutableLiveData<Boolean>()
    var isPullToRefreshLoading = MutableLiveData<Boolean>()
}
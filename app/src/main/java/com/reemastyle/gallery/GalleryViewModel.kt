package com.reemastyle.gallery

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.reemastyle.api.MyViewModel
import com.reemastyle.model.gallery.GalleryResponse

class GalleryViewModel : MyViewModel() {
    var galleryResponse = MutableLiveData<GalleryResponse>()

    fun getGalleryData(map: JsonObject) {
        isLoading.value = true
        GalleryRepository.getGalleryData({
            isLoading.value = false
            galleryResponse.value = it
        }, {
            isLoading.value = false
            apiError.value = it
        }, {
            isLoading.value = false
            unauthorized.value = it
        }, map)
    }
}
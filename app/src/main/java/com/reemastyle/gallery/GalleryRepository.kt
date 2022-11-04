package com.reemastyle.gallery

import android.util.Log
import com.google.gson.JsonObject
import com.reemastyle.api.ApiFailureTypes
import com.reemastyle.api.ApiHelper
import com.reemastyle.api.RestClient
import com.reemastyle.model.gallery.GalleryResponse
import com.reemastyle.model.packages.PackagesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GalleryRepository {
    private val webService = RestClient.get()

    fun getGalleryData(
        successHandler: (GalleryResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getGalleryData(hashMap).enqueue(object :
            Callback<GalleryResponse> {
            override fun onResponse(
                call: Call<GalleryResponse>,
                response: Response<GalleryResponse>
            ) {
                response?.body()?.let {
                    successHandler(it)
                }

                when {
                    response?.code() == 422 -> {
                        response.errorBody()?.let {
                            val error = ApiHelper.handleAuthenticationError(response.errorBody()!!)
                            failureHandler(error)
                        }
                    }
                    response?.code() == 401 -> {
                        response.errorBody()?.let {
                            unauthorized(true)
                        }
                    }
                    else -> {
                        response?.errorBody()?.let {
                            val error = ApiHelper.handleApiError(response.errorBody())
                            failureHandler(error)
                        }
                    }
                }

            }

            override fun onFailure(call: Call<GalleryResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }
}
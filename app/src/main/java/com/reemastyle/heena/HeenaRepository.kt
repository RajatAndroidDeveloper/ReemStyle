package com.reemastyle.heena

import android.util.Log
import com.google.gson.JsonObject
import com.reemastyle.api.ApiFailureTypes
import com.reemastyle.api.ApiHelper
import com.reemastyle.api.RestClient
import com.reemastyle.model.adddheenacart.AddHeenaInCartResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object HeenaRepository {
    private val webService = RestClient.get()

    fun addHeenaToCart(
        successHandler: (AddHeenaInCartResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.addHeenaToCart(hashMap).enqueue(object :
            Callback<AddHeenaInCartResponse> {
            override fun onResponse(
                call: Call<AddHeenaInCartResponse>,
                response: Response<AddHeenaInCartResponse>
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

            override fun onFailure(call: Call<AddHeenaInCartResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

}
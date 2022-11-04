package com.reemastyle.bookings

import android.util.Log
import com.google.gson.JsonObject
import com.reemastyle.api.ApiFailureTypes
import com.reemastyle.api.ApiHelper
import com.reemastyle.api.RestClient
import com.reemastyle.model.history.OrderHistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object BookingRepository {
    private val webService = RestClient.get()

    fun getAllBookings(
        successHandler: (OrderHistoryResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getAllBookings(hashMap).enqueue(object :
            Callback<OrderHistoryResponse> {
            override fun onResponse(
                call: Call<OrderHistoryResponse>,
                response: Response<OrderHistoryResponse>
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

            override fun onFailure(call: Call<OrderHistoryResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }


    fun cancelBooking(
        successHandler: (OrderHistoryResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.cancelBooking(hashMap).enqueue(object :
            Callback<OrderHistoryResponse> {
            override fun onResponse(
                call: Call<OrderHistoryResponse>,
                response: Response<OrderHistoryResponse>
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

            override fun onFailure(call: Call<OrderHistoryResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

}
package com.reemastyle.cart

import android.util.Log
import com.google.gson.JsonObject
import com.reemastyle.api.ApiFailureTypes
import com.reemastyle.api.ApiHelper
import com.reemastyle.api.RestClient
import com.reemastyle.model.address.AddAddressResponse
import com.reemastyle.model.cart.AddToCartResponse
import com.reemastyle.model.cart.SavedCartResponse
import com.reemastyle.model.slots.TimeSlotResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AddToCartRepository {
    private val webService = RestClient.get()

    fun addToCart(
        successHandler: (AddToCartResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.addToCart(hashMap).enqueue(object :
            Callback<AddToCartResponse> {
            override fun onResponse(
                call: Call<AddToCartResponse>,
                response: Response<AddToCartResponse>
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

            override fun onFailure(call: Call<AddToCartResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }


    fun getCartData(
        successHandler: (SavedCartResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getCartData(hashMap).enqueue(object :
            Callback<SavedCartResponse> {
            override fun onResponse(
                call: Call<SavedCartResponse>,
                response: Response<SavedCartResponse>
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

            override fun onFailure(call: Call<SavedCartResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

    fun deleteCartItem(
        successHandler: (SavedCartResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.deleteCartItem(hashMap).enqueue(object :
            Callback<SavedCartResponse> {
            override fun onResponse(
                call: Call<SavedCartResponse>,
                response: Response<SavedCartResponse>
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

            override fun onFailure(call: Call<SavedCartResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

    fun updateCartItem(
        successHandler: (SavedCartResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.updateCartItem(hashMap).enqueue(object :
            Callback<SavedCartResponse> {
            override fun onResponse(
                call: Call<SavedCartResponse>,
                response: Response<SavedCartResponse>
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

            override fun onFailure(call: Call<SavedCartResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

    fun placeOrder(
        successHandler: (SavedCartResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.placeOrder(hashMap).enqueue(object :
            Callback<SavedCartResponse> {
            override fun onResponse(
                call: Call<SavedCartResponse>,
                response: Response<SavedCartResponse>
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

            override fun onFailure(call: Call<SavedCartResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

    fun getAllTimeSlots(
        successHandler: (TimeSlotResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getAllTimeSlots(hashMap).enqueue(object :
            Callback<TimeSlotResponse> {
            override fun onResponse(
                call: Call<TimeSlotResponse>,
                response: Response<TimeSlotResponse>
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

            override fun onFailure(call: Call<TimeSlotResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

}
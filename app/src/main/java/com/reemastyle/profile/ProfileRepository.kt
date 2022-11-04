package com.reemastyle.profile

import android.util.Log
import com.google.gson.JsonObject
import com.reemastyle.api.ApiFailureTypes
import com.reemastyle.api.ApiHelper
import com.reemastyle.api.RestClient
import com.reemastyle.model.profile.ProfileResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProfileRepository {
    private val webService = RestClient.get()

    fun getProfile(
        successHandler: (ProfileResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getProfile(hashMap).enqueue(object :
            Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
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

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

    fun changePassword(
        successHandler: (ProfileResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.changePassword(hashMap).enqueue(object :
            Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
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

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

    fun updateProfile(
        successHandler: (ProfileResponse) -> Unit,
        failureHandler: (String) -> Unit,
        unauthorized: (Boolean) -> Unit,
        image: RequestBody,
        name: RequestBody,
        email: RequestBody,
        countryCode: RequestBody,
        mobile: RequestBody,
        id: RequestBody,
        action: RequestBody
    ) {
        webService.updateProfile(image,name,email,countryCode,mobile,id,action).enqueue(object :
            Callback<ProfileResponse> {
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
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

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }
}
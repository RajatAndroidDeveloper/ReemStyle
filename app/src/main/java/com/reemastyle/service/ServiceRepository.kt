package com.reemastyle.service

import android.util.Log
import com.google.gson.JsonObject
import com.reemastyle.api.ApiFailureTypes
import com.reemastyle.api.ApiHelper
import com.reemastyle.api.RestClient
import com.reemastyle.model.categories.CategoriesResponse
import com.reemastyle.model.heenacategories.HeenaCategoryResponse
import com.reemastyle.model.heenadetail.HeenaDetailResponse
import com.reemastyle.model.services.ServicesResponse
import com.reemastyle.model.subcategory.SubCategoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ServiceRepository {
    private val webService = RestClient.get()

    fun getCategories(
        successHandler: (CategoriesResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getCategories(hashMap).enqueue(object :
            Callback<CategoriesResponse> {
            override fun onResponse(
                call: Call<CategoriesResponse>,
                response: Response<CategoriesResponse>
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

            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

    fun getSubCategories(
        successHandler: (SubCategoriesResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getSubCategories(hashMap).enqueue(object :
            Callback<SubCategoriesResponse> {
            override fun onResponse(
                call: Call<SubCategoriesResponse>,
                response: Response<SubCategoriesResponse>
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

            override fun onFailure(call: Call<SubCategoriesResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }


    fun getHeenaCategories(
        successHandler: (HeenaCategoryResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getHeenaCategories(hashMap).enqueue(object :
            Callback<HeenaCategoryResponse> {
            override fun onResponse(
                call: Call<HeenaCategoryResponse>,
                response: Response<HeenaCategoryResponse>
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

            override fun onFailure(call: Call<HeenaCategoryResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }


    fun getHeenaDetails(
        successHandler: (HeenaDetailResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getHeenaDetails(hashMap).enqueue(object :
            Callback<HeenaDetailResponse> {
            override fun onResponse(
                call: Call<HeenaDetailResponse>,
                response: Response<HeenaDetailResponse>
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

            override fun onFailure(call: Call<HeenaDetailResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }

    fun getServices(
        successHandler: (ServicesResponse) -> Unit,
        failureHandler: (String) -> Unit, unauthorized: (Boolean) -> Unit, hashMap: JsonObject
    ) {
        webService.getServices(hashMap).enqueue(object :
            Callback<ServicesResponse> {
            override fun onResponse(
                call: Call<ServicesResponse>,
                response: Response<ServicesResponse>
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

            override fun onFailure(call: Call<ServicesResponse>, t: Throwable) {
                t.message?.let { Log.d("Error", it) }

                t.let {
                    failureHandler(ApiFailureTypes().getFailureMessage(it))
                }
            }
        })
    }
}
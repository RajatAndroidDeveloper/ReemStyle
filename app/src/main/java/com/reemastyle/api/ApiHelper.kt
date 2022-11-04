package com.reemastyle.api


import com.reemastyle.preferences.Preferences
import com.google.gson.GsonBuilder
import com.reemastyle.util.Constants.ACCESS_TOKEN
import com.reemastyle.util.Constants.BASE_URL
import com.reemastyle.util.Constants.SOMETHING_WENT_WRONG
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiHelper {
    private var mRetrofit: Retrofit
    var gson = GsonBuilder()
        .setLenient()
        .create()

    init {
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getClient())
            .build()
    }

    private fun getClient(): OkHttpClient {
           val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient().newBuilder().addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val requestBuilder: Request.Builder =
                original.newBuilder().method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }).addInterceptor(interceptor)
            .connectTimeout(3, TimeUnit.MINUTES)
            .readTimeout(3, TimeUnit.MINUTES)
            .writeTimeout(3, TimeUnit.MINUTES)
            .build()
    }

    fun createService(): WebService {
        return mRetrofit.create(WebService::class.java)
    }

    fun handleApiError(body: ResponseBody?): String {
        var errorMsg = SOMETHING_WENT_WRONG
        try {
            val errorConverter: Converter<ResponseBody, Status> =
                mRetrofit.responseBodyConverter(Status::class.java, arrayOfNulls(0))
            val error: Status = errorConverter.convert(body)!!
            errorMsg = error.message
        } catch (e: Exception) {
        }
        return errorMsg
    }

    fun handleAuthenticationError(body: ResponseBody?): String {
        val errorConverter: Converter<ResponseBody, ErrorResponse> =
            mRetrofit.responseBodyConverter(ErrorResponse::class.java, arrayOfNulls(0))
        val errorResponse: ErrorResponse = errorConverter.convert(body)!!
        var errorMsg = errorResponse.message!!
        val email = errorResponse.errors?.email
        val password = errorResponse.errors?.password
        val role = errorResponse.errors?.role
        val deviceToken = errorResponse.errors?.deviceToken
        val mobileNumber = errorResponse.errors?.mobile

        if (email != null && email.isNotEmpty()) {
            errorMsg = email[0].toString()
        } else if (password != null && password.isNotEmpty()) {
            errorMsg = password[0].toString()
        } else if (role != null && role.isNotEmpty()) {
            errorMsg = role[0].toString()
        } else if (deviceToken != null && deviceToken.isNotEmpty()) {
            errorMsg = (deviceToken[0].toString())
        } else if (mobileNumber != null && mobileNumber.isNotEmpty()) {
            errorMsg = mobileNumber[0].toString()
        }
        return errorMsg
    }
}
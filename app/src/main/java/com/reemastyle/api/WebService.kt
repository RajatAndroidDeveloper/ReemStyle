package com.reemastyle.api

import com.google.gson.JsonObject
import com.reemastyle.model.adddheenacart.AddHeenaInCartResponse
import com.reemastyle.model.address.AddAddressResponse
import com.reemastyle.model.cart.AddToCartResponse
import com.reemastyle.model.cart.SavedCartResponse
import com.reemastyle.model.categories.CategoriesResponse
import com.reemastyle.model.gallery.GalleryResponse
import com.reemastyle.model.getaddress.GetAddressResponse
import com.reemastyle.model.heenacategories.HeenaCategoryResponse
import com.reemastyle.model.heenadetail.HeenaDetailResponse
import com.reemastyle.model.history.OrderHistoryResponse
import com.reemastyle.model.home.HomeResponse
import com.reemastyle.model.login.LoginResponse
import com.reemastyle.model.notification.NotificationResponse
import com.reemastyle.model.packagedetail.PackageDetailsResponse
import com.reemastyle.model.packages.PackagesResponse
import com.reemastyle.model.profile.ProfileResponse
import com.reemastyle.model.search.SearchCategoryResponse
import com.reemastyle.model.services.ServicesResponse
import com.reemastyle.model.slots.TimeSlotResponse
import com.reemastyle.model.subcategory.SubCategoriesResponse
import com.reemastyle.model.zones.ZonesResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface WebService {
    @POST("index.php")
    fun registerUser(
        @Body map: JsonObject
    ): Call<LoginResponse>

    @POST("index.php")
    fun loginUser(
        @Body map: JsonObject
    ): Call<LoginResponse>

    @POST("index.php")
    fun verifyOtp(
        @Body map: JsonObject
    ): Call<LoginResponse>

    @POST("index.php")
    fun resendOtp(
        @Body map: JsonObject
    ): Call<LoginResponse>

    @POST("index.php")
    fun forgotPasswordEmailRequest(
        @Body map: JsonObject
    ): Call<LoginResponse>

    @POST("index.php")
    fun getProfile(
        @Body map: JsonObject
    ): Call<ProfileResponse>

    @POST("index.php")
    fun changePassword(
        @Body map: JsonObject
    ): Call<ProfileResponse>

    @Multipart
    @POST("index.php")
    fun updateProfile(
        @Part("prof_image\"; filename=\"pp1.png\" ") pic: RequestBody,
        @Part("name")name: RequestBody,
        @Part("email")email: RequestBody,
        @Part("ext")countryCode: RequestBody,
        @Part("phone")mobileNumber: RequestBody,
        @Part("user_id")id: RequestBody,
        @Part("action")action: RequestBody,
    ): Call<LoginResponse>

    @POST("index.php")
    fun getHomeData(
        @Body map: JsonObject
    ): Call<HomeResponse>

    @POST("index.php")
    fun getCategories(
        @Body map: JsonObject
    ): Call<CategoriesResponse>

    @POST("index.php")
    fun getSubCategories(
        @Body map: JsonObject
    ): Call<SubCategoriesResponse>

    @POST("index.php")
    fun getHeenaCategories(
        @Body map: JsonObject
    ): Call<HeenaCategoryResponse>

    @POST("index.php")
    fun addHeenaToCart(
        @Body map: JsonObject
    ): Call<AddHeenaInCartResponse>

    @POST("index.php")
    fun getHeenaDetails(
        @Body map: JsonObject
    ): Call<HeenaDetailResponse>

    @POST("index.php")
    fun getServices(
        @Body map: JsonObject
    ): Call<ServicesResponse>

    @POST("index.php")
    fun getPackageData(
        @Body map: JsonObject
    ): Call<PackagesResponse>

    @POST("index.php")
    fun getPackageDetails(
        @Body map: JsonObject
    ): Call<PackageDetailsResponse>

    @POST("index.php")
    fun getGalleryData(
        @Body map: JsonObject
    ): Call<GalleryResponse>

    @POST("index.php")
    fun addAddress(
        @Body map: JsonObject
    ): Call<AddAddressResponse>

    @POST("index.php")
    fun getAllAddress(
        @Body map: JsonObject
    ): Call<GetAddressResponse>

    @POST("index.php")
    fun getAllZones(
        @Body map: JsonObject
    ): Call<ZonesResponse>

    @POST("index.php")
    fun searchcategory(
        @Body map: JsonObject
    ): Call<SearchCategoryResponse>

    @POST("index.php")
    fun getNotifications(
        @Body map: JsonObject
    ): Call<NotificationResponse>

    @POST("index.php")
    fun addToCart(
        @Body map: JsonObject
    ): Call<AddToCartResponse>

    @POST("index.php")
    fun getAllBookings(
        @Body map: JsonObject
    ): Call<OrderHistoryResponse>

    @POST("index.php")
    fun cancelBooking(
        @Body map: JsonObject
    ): Call<OrderHistoryResponse>

    @POST("index.php")
    fun getCartData(
        @Body map: JsonObject
    ): Call<SavedCartResponse>

    @POST("index.php")
    fun deleteCartItem(
        @Body map: JsonObject
    ): Call<SavedCartResponse>

    @POST("index.php")
    fun updateCartItem(
        @Body map: JsonObject
    ): Call<SavedCartResponse>

    @POST("index.php")
    fun placeOrder(
        @Body map: JsonObject
    ): Call<SavedCartResponse>

    @POST("index.php")
    fun getAllTimeSlots(
        @Body map: JsonObject
    ): Call<TimeSlotResponse>
}
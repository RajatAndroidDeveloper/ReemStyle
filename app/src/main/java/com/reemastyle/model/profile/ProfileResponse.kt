package com.reemastyle.model.profile

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("profile_data")
	val profileData: ProfileData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ProfileData(

	@field:SerializedName("ext")
	val ext: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("verifiycode")
	val verifiycode: String? = null,

	@field:SerializedName("phone")
	val phone: String? = null,

	@field:SerializedName("prof_image")
	val profImage: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("active")
	val active: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("firebase_token")
	val firebaseToken: String? = null,

	@field:SerializedName("device")
	val device: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

package com.reemastyle.model.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("userdata")
	val userdata: Userdata? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Userdata(

	@field:SerializedName("ext")
	var ext: String? = null,

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("verifiycode")
	val verifiycode: String? = null,

	@field:SerializedName("phone")
	var phone: String? = null,

	@field:SerializedName("prof_image")
    var profImage: String? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("active")
	val active: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("firebase_token")
	val firebaseToken: String? = null,

	@field:SerializedName("device")
	val device: String? = null,

	@field:SerializedName("email")
	var email: String? = null
)

package com.reemastyle.model.notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(

	@field:SerializedName("noti")
	val noti: List<NotiItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class Userdata(

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

data class NotiItem(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("totype")
	val totype: String? = null,

	@field:SerializedName("userdata")
	val userdata: Userdata? = null,

	@field:SerializedName("reportid")
	val reportid: String? = null,

	@field:SerializedName("response_status")
	val responseStatus: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("response_id")
	val responseId: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("device")
	val device: String? = null
)

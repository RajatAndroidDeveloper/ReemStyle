package com.reemastyle.model.slots

import com.google.gson.annotations.SerializedName

data class TimeSlotResponse(

	@field:SerializedName("slots")
	val slots: List<SlotsItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SlotsItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

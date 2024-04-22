package com.dmonster.data.remote.dto.response.home

import com.google.gson.annotations.SerializedName

data class CreatorDto(
    @SerializedName("ct_name") val ctName: String?,
    @SerializedName("ct_code") val ctCode: String?,
    @SerializedName("ct_order") val ctOrder: Int?,
)

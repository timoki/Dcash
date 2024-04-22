package com.dmonster.data.remote.dto.response.home

import com.google.gson.annotations.SerializedName

data class AuthorDto(
    @SerializedName("at_name") val atName: String?,
    @SerializedName("at_code") val atCode: String?,
    @SerializedName("at_order") val atOrder: Int?,
)

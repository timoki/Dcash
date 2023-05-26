package com.dmonster.data.remote.dto.response

import com.dmonster.data.remote.dto.response.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("accessToken") val accessToken: String? = null,
    @SerializedName("refreshToken") val refreshToken: String? = null,
) : BaseResponse<TokenDto>()
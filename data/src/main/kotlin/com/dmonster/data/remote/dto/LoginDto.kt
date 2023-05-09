package com.dmonster.data.remote.dto

import com.dmonster.data.remote.dto.base.BaseResponse

data class LoginDto(
    val accessToken: String? = null,
    val refreshToken: String? = null,
) : BaseResponse()
package com.dmonster.data.remote.dto.base

import com.google.gson.annotations.SerializedName

data class BaseData<T>(
    @SerializedName("data") val data: T? = null
) : BaseResponse()
package com.dmonster.data.remote.dto.response.base

import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {
    @SerializedName("result")
    val result: String? = null

    @SerializedName("resultDetail")
    val resultDetail: String? = null

    @SerializedName("status")
    val status: String? = null

    @SerializedName("statusCode")
    val statusCode: Int? = null

    @SerializedName("dataType")
    val dataType: String? = null

    @SerializedName("data")
    val data: T? = null

    fun isSuccess(): Boolean {
        return result == "SUCCESS"
    }
}
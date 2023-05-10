package com.dmonster.data.remote.dto.base

import com.google.gson.annotations.SerializedName

open class BaseResponse {
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

    fun isSuccess(): Boolean {
        return result == "SUCCESS"
    }
}
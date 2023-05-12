package com.dmonster.data.remote.dto

import com.dmonster.data.remote.dto.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class MemberInfoDto(
    @SerializedName("idx") val idx: Int,
    @SerializedName("mt_id") val mt_id: String? = null,
    @SerializedName("mt_name") val mt_name: String? = null,
    @SerializedName("mt_wdate") val mt_wdate: String? = null,
    @SerializedName("mt_type") val mt_type: Int? = null,
) : BaseResponse<MemberInfoDto>()
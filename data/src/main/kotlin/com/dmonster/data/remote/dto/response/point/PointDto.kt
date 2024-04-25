package com.dmonster.data.remote.dto.response.point

import com.dmonster.data.remote.dto.response.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class PointDto(
    @SerializedName("point") val point: Int? = null,
    @SerializedName("hisotry") val hisotry: List<PointHistoryDto>? = null,
) : BaseResponse<PointDto>()
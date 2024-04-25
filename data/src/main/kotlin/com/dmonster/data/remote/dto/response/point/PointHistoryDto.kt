package com.dmonster.data.remote.dto.response.point

import com.google.gson.annotations.SerializedName

data class PointHistoryDto(
    @SerializedName("mt_idx") val mtIdx: Int,
    @SerializedName("pt_type") val ptType: Int? = null,
    @SerializedName("pt_name") val ptName: String? = null,
    @SerializedName("pt_point") val ptPoint: Int? = null,
    @SerializedName("pt_wdate") val ptWdate: String? = null,
    @SerializedName("guid") val guid: Long? = null,
)
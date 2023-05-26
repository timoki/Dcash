package com.dmonster.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class PageDto(
    @SerializedName("search_value") val search_value: String?,
    @SerializedName("search_edate2") val search_edate2: String?,
    @SerializedName("offset") val offset: Int?,
    @SerializedName("end_page") val end_page: Int?,
    @SerializedName("total_count") val total_count: Int?,
    @SerializedName("search_order") val search_order: String?,
    @SerializedName("cur_page") val cur_page: Int,
    @SerializedName("search_type") val search_type: String?,
    @SerializedName("search_edate") val search_edate: String?,
    @SerializedName("search_sdate2") val search_sdate2: String?,
    @SerializedName("start_page") val start_page: Int?,
    @SerializedName("fetch") val fetch: Int?,
    @SerializedName("search_filter") val search_filter: String?,
    @SerializedName("total_page") val total_page: Int?,
    @SerializedName("row") val row: Int?,
    @SerializedName("search_sdate") val search_sdate: String?,
)

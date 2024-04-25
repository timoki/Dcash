package com.dmonster.data.remote.api

import com.dmonster.data.remote.dto.response.base.BaseResponse
import com.dmonster.data.remote.dto.response.point.PointDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST

interface PointAPIService {
    companion object {
        private const val API_ROUTE = "point"
    }

    @POST(API_ROUTE)
    suspend fun getUserPoint(): Response<BaseResponse<PointDto>>

    @POST("$API_ROUTE-history")
    suspend fun getPointHistory(
        @Field("pg") pg: Int,
        @Field("row") row: Int,
        @Field("search_filter") search_filter: String?,
        @Field("search_sdate") search_sdate: String?,
        @Field("search_edate") search_edate: String?,
        @Field("search_order") search_order: String?,
    ): Response<BaseResponse<PointDto>>
}
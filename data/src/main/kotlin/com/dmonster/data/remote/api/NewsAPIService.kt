package com.dmonster.data.remote.api

import com.dmonster.data.remote.dto.response.news.NewsDto
import com.dmonster.data.remote.dto.response.base.BaseResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface NewsAPIService {
    companion object {
        private const val API_ROUTE = "news"
    }

    @FormUrlEncoded
    @POST("$API_ROUTE")
    suspend fun getNewsList(
        @Field("pg") pg: Int,
        @Field("row") row: Int,
        @Field("search_filter") search_filter: String?,
        @Field("search_value") search_value: String?,
        @Field("search_sdate") search_sdate: String?,
        @Field("search_edate") search_edate: String?,
        @Field("search_order") search_order: String?,
    ): Response<BaseResponse<NewsDto>>
}
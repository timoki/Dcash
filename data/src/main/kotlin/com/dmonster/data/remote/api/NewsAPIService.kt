package com.dmonster.data.remote.api

import com.dmonster.data.remote.dto.NewsDto
import com.dmonster.data.remote.dto.base.BaseResponse
import retrofit2.Response
import retrofit2.http.POST

interface NewsAPIService {
    companion object {
        private const val API_ROUTE = "news"
    }

    @POST("$API_ROUTE")
    suspend fun getNewsList(): Response<BaseResponse<List<NewsDto>>>
}
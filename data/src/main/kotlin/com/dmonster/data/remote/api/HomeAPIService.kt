package com.dmonster.data.remote.api

import com.dmonster.data.remote.dto.response.base.BaseResponse
import com.dmonster.data.remote.dto.response.home.HomeDataDto
import retrofit2.Response
import retrofit2.http.POST

interface HomeAPIService {
    companion object {
        private const val API_ROUTE = "home"
    }

    @POST(API_ROUTE)
    suspend fun getHomeData(): Response<BaseResponse<HomeDataDto>>
}
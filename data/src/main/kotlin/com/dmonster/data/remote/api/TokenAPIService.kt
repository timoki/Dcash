package com.dmonster.data.remote.api

import com.dmonster.data.remote.dto.TokenDto
import com.dmonster.data.remote.dto.base.BaseData
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface TokenAPIService {
    companion object {
        private const val API_ROUTE = "token"
    }

    @FormUrlEncoded
    @POST("$API_ROUTE/access")
    suspend fun getAccessToken(
        @Field("refreshToken") refreshToken: String,
    ): Response<TokenDto>

    @FormUrlEncoded
    @POST("$API_ROUTE/refresh")
    suspend fun changeRefreshToken(
        @Field("refreshToken") refreshToken: String,
    ): Response<TokenDto>
}
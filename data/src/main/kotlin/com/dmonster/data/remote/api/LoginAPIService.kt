package com.dmonster.data.remote.api

import com.dmonster.data.remote.dto.TokenDto
import com.dmonster.data.remote.dto.base.BaseData
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginAPIService {
    companion object {
        private const val API_ROUTE = "login"
    }

    @FormUrlEncoded
    @POST("$API_ROUTE")
    suspend fun userLogin(
        @Field("mt_id") mt_id: String,
        @Field("mt_pwd") mt_pwd: String,
    ): Response<TokenDto>
}
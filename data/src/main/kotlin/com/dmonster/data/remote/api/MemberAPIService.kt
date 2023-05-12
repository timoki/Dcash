package com.dmonster.data.remote.api

import com.dmonster.data.remote.dto.MemberInfoDto
import com.dmonster.data.remote.dto.base.BaseResponse
import retrofit2.Response
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MemberAPIService {
    companion object {
        private const val API_ROUTE = "member"
    }

    @POST("$API_ROUTE/info")
    suspend fun getMemberInfo(): Response<BaseResponse<MemberInfoDto>>
}
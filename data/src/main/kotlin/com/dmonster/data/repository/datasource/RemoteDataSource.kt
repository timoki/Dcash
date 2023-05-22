package com.dmonster.data.repository.datasource

import com.dmonster.data.remote.dto.MemberInfoDto
import com.dmonster.data.remote.dto.NewsDto
import com.dmonster.data.remote.dto.TokenDto
import com.dmonster.data.remote.dto.base.BaseResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun requestLogin(
        id: String, pw: String
    ): Response<BaseResponse<TokenDto>>

    suspend fun getAccessToken(
        refreshToken: String
    ): Response<BaseResponse<TokenDto>>

    suspend fun changeRefreshToken(
        refreshToken: String
    ): Response<BaseResponse<TokenDto>>

    suspend fun getMemberInfo(): Response<BaseResponse<MemberInfoDto>>

    suspend fun getNewsList(): Response<BaseResponse<List<NewsDto>>>
}
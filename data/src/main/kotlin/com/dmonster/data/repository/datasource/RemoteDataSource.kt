package com.dmonster.data.repository.datasource

import com.dmonster.data.remote.dto.MemberInfoDto
import com.dmonster.data.remote.dto.TokenDto
import retrofit2.Response

interface RemoteDataSource {
    suspend fun requestLogin(
        id: String, pw: String
    ): Response<TokenDto>

    suspend fun getAccessToken(
        refreshToken: String
    ): Response<TokenDto>

    suspend fun changeRefreshToken(
        refreshToken: String
    ): Response<TokenDto>

    suspend fun getMemberInfo(): Response<MemberInfoDto>
}
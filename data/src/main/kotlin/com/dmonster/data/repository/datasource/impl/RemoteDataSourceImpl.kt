package com.dmonster.data.repository.datasource.impl

import com.dmonster.data.remote.api.LoginAPIService
import com.dmonster.data.remote.api.MemberAPIService
import com.dmonster.data.remote.api.TokenAPIService
import com.dmonster.data.remote.dto.MemberInfoDto
import com.dmonster.data.remote.dto.TokenDto
import com.dmonster.data.remote.dto.base.BaseResponse
import com.dmonster.data.repository.datasource.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val loginAPIService: LoginAPIService,
    private val tokenAPIService: TokenAPIService,
    private val memberAPIService: MemberAPIService,
) : RemoteDataSource {
    override suspend fun requestLogin(id: String, pw: String): Response<BaseResponse<TokenDto>> {
        return loginAPIService.userLogin(id, pw)
    }

    override suspend fun getAccessToken(refreshToken: String): Response<BaseResponse<TokenDto>> {
        return tokenAPIService.getAccessToken(refreshToken)
    }

    override suspend fun changeRefreshToken(refreshToken: String): Response<BaseResponse<TokenDto>> {
        return tokenAPIService.changeRefreshToken(refreshToken)
    }

    override suspend fun getMemberInfo(): Response<BaseResponse<MemberInfoDto>> {
        return memberAPIService.getMemberInfo()
    }
}
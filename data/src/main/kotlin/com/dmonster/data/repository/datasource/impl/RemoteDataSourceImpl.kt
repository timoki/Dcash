package com.dmonster.data.repository.datasource.impl

import com.dmonster.data.remote.api.HomeAPIService
import com.dmonster.data.remote.api.LoginAPIService
import com.dmonster.data.remote.api.MemberAPIService
import com.dmonster.data.remote.api.NewsAPIService
import com.dmonster.data.remote.api.TokenAPIService
import com.dmonster.data.remote.dto.response.MemberInfoDto
import com.dmonster.data.remote.dto.response.TokenDto
import com.dmonster.data.remote.dto.response.base.BaseResponse
import com.dmonster.data.remote.dto.response.home.HomeDataDto
import com.dmonster.data.remote.dto.response.news.NewsDto
import com.dmonster.data.repository.datasource.RemoteDataSource
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val loginAPIService: LoginAPIService,
    private val tokenAPIService: TokenAPIService,
    private val memberAPIService: MemberAPIService,
    private val newsAPIService: NewsAPIService,
    private val homeAPIService: HomeAPIService,
) : RemoteDataSource {
    override suspend fun getHomeData(): Response<BaseResponse<HomeDataDto>> {
        return homeAPIService.getHomeData()
    }

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

    override suspend fun getNewsList(
        pg: Int,
        row: Int,
        search_filter: String?,
        search_value: String?,
        search_sdate: String?,
        search_edate: String?,
        search_order: String?,
        search_category: String?,
        search_author: String?,
        search_creator: String?,
    ): Response<BaseResponse<NewsDto>> {
        return newsAPIService.getNewsList(
            pg,
            row,
            search_filter,
            search_value,
            search_sdate,
            search_edate,
            search_order,
            search_category,
            search_author,
            search_creator,
        )
    }
}
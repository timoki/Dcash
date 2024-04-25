package com.dmonster.data.repository.datasource

import com.dmonster.data.remote.dto.response.MemberInfoDto
import com.dmonster.data.remote.dto.response.TokenDto
import com.dmonster.data.remote.dto.response.base.BaseResponse
import com.dmonster.data.remote.dto.response.home.HomeDataDto
import com.dmonster.data.remote.dto.response.news.NewsDto
import com.dmonster.data.remote.dto.response.point.PointDto
import retrofit2.Response

interface RemoteDataSource {

    suspend fun getPointHistory(
        pg: Int,
        row: Int,
        search_filter: String?,
        search_sdate: String?,
        search_edate: String?,
        search_order: String?,
    ): Response<BaseResponse<PointDto>>

    suspend fun getUserPoint(): Response<BaseResponse<PointDto>>

    suspend fun getHomeData(): Response<BaseResponse<HomeDataDto>>

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

    suspend fun getNewsList(
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
    ): Response<BaseResponse<NewsDto>>

    suspend fun viewNews(
        guid: Long,
    ): Response<BaseResponse<Any>>
}
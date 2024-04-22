package com.dmonster.data.remote.dto.response.home

import com.dmonster.data.remote.dto.response.base.BaseResponse
import com.dmonster.data.remote.dto.response.news.NewsListDto
import com.google.gson.annotations.SerializedName

data class HomeDataDto(
    @SerializedName("cretor") val creator: List<CreatorDto>?,
    @SerializedName("author") val author: List<AuthorDto>?,
    @SerializedName("category") val category: List<CategoryDto>?,
    @SerializedName("custom") val custom: List<NewsListDto>?,
    @SerializedName("banner") val banner: List<NewsListDto>?,
    @SerializedName("categoryNews") val categoryNews: List<CategoryNewsDto>?,
    @SerializedName("recommand") val recommend: List<NewsListDto>?,
) : BaseResponse<HomeDataDto>()

package com.dmonster.data.remote.dto.response.home

import com.dmonster.data.remote.dto.response.news.NewsListDto
import com.google.gson.annotations.SerializedName

data class CategoryNewsDto(
    @SerializedName("ct_name") val ctName: String?,
    @SerializedName("news") val news: List<NewsListDto>?,
)

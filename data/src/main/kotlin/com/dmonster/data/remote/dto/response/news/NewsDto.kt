package com.dmonster.data.remote.dto.response.news

import com.dmonster.data.remote.dto.response.PageDto
import com.dmonster.data.remote.dto.response.base.BaseResponse
import com.google.gson.annotations.SerializedName

data class NewsDto(
    @SerializedName("page") val page: PageDto,
    @SerializedName("rows") val rows: List<NewsListDto>
): BaseResponse<NewsDto>()

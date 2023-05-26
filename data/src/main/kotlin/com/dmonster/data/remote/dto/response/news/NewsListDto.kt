package com.dmonster.data.remote.dto.response.news

import com.google.gson.annotations.SerializedName

data class NewsListDto(
    @SerializedName("guid") val guid: Long,
    @SerializedName("title") val title: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("link") val link: String? = null,
    @SerializedName("enclosure") val enclosure: String? = null,
    @SerializedName("author") val author: String? = null,
    @SerializedName("creator") val creator: String? = null,
    @SerializedName("pubDate") val pubDate: String? = null,
)
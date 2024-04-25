package com.dmonster.domain.model.paging.news

import java.io.Serializable

data class NewsListModel(
    val guid: Long,
    val title: String? = null,
    val category: String? = null,
    val link: String? = null,
    val enclosure: String? = null,
    val author: String? = null,
    val creator: String? = null,
    val pubDate: String? = null,
    val point: Int? = null,
    var viewed: Int? = null,
) : Serializable

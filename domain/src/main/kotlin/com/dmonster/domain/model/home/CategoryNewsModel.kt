package com.dmonster.domain.model.home

import com.dmonster.domain.model.base.BaseModel
import com.dmonster.domain.model.paging.news.NewsListModel

data class CategoryNewsModel(
    val ctName: String?,
    val news: List<NewsListModel>?,
) : BaseModel {
    override val key: String?
        get() = ctName
}

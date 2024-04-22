package com.dmonster.domain.model.home

import com.dmonster.domain.model.base.BaseModel
import com.dmonster.domain.model.paging.news.NewsListModel

data class HomeDataModel(
    val creator: List<FilterModel>?,
    val author: List<FilterModel>?,
    val category: List<FilterModel>?,
    val custom: List<NewsListModel>?,
    val banner: List<NewsListModel>?,
    val categoryNews: List<CategoryNewsModel>?,
    val recommend: List<NewsListModel>?,
) : BaseModel {
    override val key: Long
        get() = 0
}

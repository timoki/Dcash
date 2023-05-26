package com.dmonster.domain.repository

import androidx.paging.PagingData
import com.dmonster.domain.model.paging.PageModel
import com.dmonster.domain.model.paging.news.NewsListModel
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNewsList(
        row: Int = 20,
        search_filter: String?,
        search_value: String?,
        search_sdate: String?,
        search_edate: String?,
        search_order: String?,
    ): Flow<PagingData<NewsListModel>>
}
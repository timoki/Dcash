package com.dmonster.domain.repository

import androidx.paging.PagingData
import com.dmonster.domain.model.paging.news.NewsListModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsRepository {
    fun getNewsList(
        row: Int = 20,
        search_filter: StateFlow<String?>?,
        search_value: StateFlow<String?>?,
        search_sdate: StateFlow<String?>?,
        search_edate: StateFlow<String?>?,
        search_order: StateFlow<String?>?,
        search_category: StateFlow<String?>?,
        search_author: StateFlow<String?>?,
        search_creator: StateFlow<String?>?,
    ): Flow<PagingData<NewsListModel>>
}
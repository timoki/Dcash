package com.dmonster.domain.usecase

import androidx.paging.PagingData
import com.dmonster.domain.model.paging.news.NewsListModel
import com.dmonster.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(
        row: Int = 20,
        search_filter: StateFlow<String?>? = null,
        search_value: StateFlow<String?>? = null,
        search_sdate: StateFlow<String?>? = null,
        search_edate: StateFlow<String?>? = null,
        search_order: StateFlow<String?>? = null,
        search_category: StateFlow<String?>? = null,
        search_author: StateFlow<String?>? = null,
        search_creator: StateFlow<String?>? = null,
    ): Flow<PagingData<NewsListModel>> = newsRepository.getNewsList(
        row,
        search_filter,
        search_value,
        search_sdate,
        search_edate,
        search_order,
        search_category,
        search_author,
        search_creator,
    )
}
package com.dmonster.domain.usecase

import androidx.paging.PagingData
import com.dmonster.domain.model.paging.PageModel
import com.dmonster.domain.model.paging.news.NewsListModel
import com.dmonster.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(
        row: Int = 20,
        search_filter: String? = null,
        search_value: String? = null,
        search_sdate: String? = null,
        search_edate: String? = null,
        search_order: String? = null,
    ): Flow<PagingData<NewsListModel>> = newsRepository.getNewsList(
        row,
        search_filter,
        search_value,
        search_sdate,
        search_edate,
        search_order,
    )
}
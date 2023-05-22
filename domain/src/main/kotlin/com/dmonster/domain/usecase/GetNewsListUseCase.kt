package com.dmonster.domain.usecase

import androidx.paging.PagingData
import com.dmonster.domain.model.NewsModel
import com.dmonster.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(): Flow<PagingData<NewsModel>> = newsRepository.getNewsList()
}
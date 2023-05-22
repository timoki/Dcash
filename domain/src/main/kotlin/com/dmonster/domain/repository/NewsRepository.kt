package com.dmonster.domain.repository

import androidx.paging.PagingData
import com.dmonster.domain.model.NewsModel
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getNewsList(): Flow<PagingData<NewsModel>>
}
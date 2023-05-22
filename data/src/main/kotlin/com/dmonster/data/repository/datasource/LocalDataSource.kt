package com.dmonster.data.repository.datasource

import androidx.paging.PagingSource
import com.dmonster.data.local.entity.paging.RemoteKeys
import com.dmonster.data.local.entity.paging.news.NewsEntity

interface LocalDataSource {
    fun getNewsList(): PagingSource<Int, NewsEntity>

    suspend fun deleteAllNewsList()

    suspend fun insertNewsList(list: List<NewsEntity>)

    suspend fun getRemoteKeys(type: String, idx: Long): RemoteKeys?

    suspend fun deleteRemoteKeys(type: String)

    suspend fun insertRemoteKeys(list: List<RemoteKeys>)
}
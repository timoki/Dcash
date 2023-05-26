package com.dmonster.data.repository.datasource

import androidx.paging.PagingSource
import com.dmonster.data.local.entity.paging.RemoteKeys
import com.dmonster.data.local.entity.paging.news.NewsListEntity

interface LocalDataSource {
    fun getNewsList(): PagingSource<Int, NewsListEntity>

    suspend fun deleteAllNewsList()

    suspend fun insertNewsList(list: List<NewsListEntity>)

    suspend fun getRemoteKeys(type: String, idx: Long): RemoteKeys?

    suspend fun deleteRemoteKeys(type: String)

    suspend fun insertRemoteKeys(list: List<RemoteKeys>?)
}
package com.dmonster.data.repository.datasource.impl

import androidx.paging.PagingSource
import com.dmonster.data.local.dao.ConfigDao
import com.dmonster.data.local.dao.paging.NewsDao
import com.dmonster.data.local.dao.paging.RemoteKeysDao
import com.dmonster.data.local.entity.paging.RemoteKeys
import com.dmonster.data.local.entity.paging.news.NewsEntity
import com.dmonster.data.repository.datasource.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val configDao: ConfigDao,
    private val newsDao: NewsDao,
    private val remoteKeys: RemoteKeysDao
) : LocalDataSource {
    override fun getNewsList(): PagingSource<Int, NewsEntity> {
        return newsDao.getNewsList()
    }

    override suspend fun deleteAllNewsList() {
        return newsDao.deleteAll()
    }

    override suspend fun insertNewsList(list: List<NewsEntity>) {
        return newsDao.insertAll(list)
    }

    override suspend fun getRemoteKeys(type: String, idx: Long): RemoteKeys? {
        return remoteKeys.getRemoteKeys(type, idx)
    }

    override suspend fun deleteRemoteKeys(type: String) {
        return remoteKeys.removeRemoteKeys(type)
    }

    override suspend fun insertRemoteKeys(list: List<RemoteKeys>) {
        return remoteKeys.insertRemoteKeys(list)
    }
}
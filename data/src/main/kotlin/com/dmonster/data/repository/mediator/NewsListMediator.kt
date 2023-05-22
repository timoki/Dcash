package com.dmonster.data.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dmonster.data.local.database.MyDatabase
import com.dmonster.data.local.entity.paging.RemoteKeys
import com.dmonster.data.local.entity.paging.news.NewsEntity
import com.dmonster.data.repository.datasource.LocalDataSource
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.data.utils.ObjectMapper.toNewsEntityList
import com.dmonster.domain.type.RemoteKeysType
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NewsListMediator @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val database: MyDatabase,
    private val errorCallback: ErrorCallback,
) : RemoteMediator<Int, NewsEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: remoteKeys?.prevKey?.plus(1) ?: 0
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = remoteDataSource.getNewsList()

            if (response.isSuccessful) {
                response.body()?.let {
                    if (!it.isSuccess()) {
                        errorCallback.postErrorData(it)
                    }
                }
                val news = response.body()?.data ?: return MediatorResult.Error(Exception())
                val endOfPaginationReached = news.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        localDataSource.deleteRemoteKeys(RemoteKeysType.NEWS.name)
                        localDataSource.deleteAllNewsList()
                    }

                    val prevKey = if (page == 0) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1

                    val keys = news.map {
                        RemoteKeys(
                            idx = it.guid,
                            type = RemoteKeysType.NEWS.name,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }

                    localDataSource.insertRemoteKeys(keys)
                    localDataSource.insertNewsList(news.toNewsEntityList())
                }

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

            return MediatorResult.Error(Exception())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, NewsEntity>
    ): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            localDataSource.getRemoteKeys(RemoteKeysType.NEWS.name, it.guid)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, NewsEntity>
    ): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let {
            localDataSource.getRemoteKeys(RemoteKeysType.NEWS.name, it.guid)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, NewsEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.guid?.let {
                localDataSource.getRemoteKeys(RemoteKeysType.NEWS.name, it)
            }
        }
    }
}
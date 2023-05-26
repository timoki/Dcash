package com.dmonster.data.repository.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dmonster.data.local.database.MyDatabase
import com.dmonster.data.local.entity.paging.RemoteKeys
import com.dmonster.data.local.entity.paging.news.NewsListEntity
import com.dmonster.data.remote.dto.request.NewsRequest
import com.dmonster.data.repository.datasource.LocalDataSource
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.data.utils.ObjectMapper.dtoToNewsListEntityList
import com.dmonster.domain.type.RemoteKeysType
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class NewsListMediator @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val database: MyDatabase,
    private val errorCallback: ErrorCallback,
    private val newsRequest: NewsRequest,
) : RemoteMediator<Int, NewsListEntity>() {
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, NewsListEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                Log.d("아외안되", "REFRESH -> remote is null : ${remoteKeys == null} / remoteKey : $remoteKeys")
                remoteKeys?.nextKey?.minus(1) ?: remoteKeys?.prevKey?.plus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                Log.d("아외안되", "PREPEND -> remote is null : ${remoteKeys == null} / remoteKey : $remoteKeys")
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)

                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                Log.d("아외안되", "APPEND -> remote is null : ${remoteKeys == null} / remoteKey : $remoteKeys")
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)

                nextKey
            }
        }

        try {
            val response = remoteDataSource.getNewsList(
                pg = page,
                row = newsRequest.row,
                search_filter = newsRequest.search_filter,
                search_value = newsRequest.search_value,
                search_sdate = newsRequest.search_sdate,
                search_edate = newsRequest.search_edate,
                search_order = newsRequest.search_order,
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    if (!it.isSuccess()) {
                        errorCallback.postErrorData(it)
                    }
                }
                val news = response.body()?.data ?: return MediatorResult.Error(Exception())
                val endOfPaginationReached = news.rows.isNullOrEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        Log.d("아외안되", "LoadType.REFRESH")
                        localDataSource.deleteRemoteKeys(RemoteKeysType.NEWS.name)
                        localDataSource.deleteAllNewsList()
                    }

                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1

                    val keys = news.rows.map {
                        RemoteKeys(
                            idx = it.guid,
                            type = RemoteKeysType.NEWS.name,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }

                    localDataSource.insertRemoteKeys(keys)
                    localDataSource.insertNewsList(news.rows.dtoToNewsListEntityList())
                }

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            }

            return MediatorResult.Error(Exception())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, NewsListEntity>
    ): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            localDataSource.getRemoteKeys(RemoteKeysType.NEWS.name, it.guid)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, NewsListEntity>
    ): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let {
            localDataSource.getRemoteKeys(RemoteKeysType.NEWS.name, it.guid)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, NewsListEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.guid?.let {
                localDataSource.getRemoteKeys(RemoteKeysType.NEWS.name, it)
            }
        }
    }
}
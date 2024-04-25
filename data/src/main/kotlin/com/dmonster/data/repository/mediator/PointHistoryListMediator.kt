package com.dmonster.data.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dmonster.data.local.database.MyDatabase
import com.dmonster.data.local.entity.paging.RemoteKeys
import com.dmonster.data.local.entity.paging.point.PointHistoryEntity
import com.dmonster.data.remote.dto.request.PagingRequest
import com.dmonster.data.repository.datasource.LocalDataSource
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.data.utils.ObjectMapper.dtoToPointHistoryEntityList
import com.dmonster.domain.type.RemoteKeysType
import com.dmonster.domain.type.TokenErrorType
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PointHistoryListMediator @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val database: MyDatabase,
    private val errorCallback: ErrorCallback,
    private val pagingRequest: PagingRequest,
) : RemoteMediator<Int, PointHistoryEntity>() {
    override suspend fun load(
        loadType: LoadType, state: PagingState<Int, PointHistoryEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: remoteKeys?.prevKey?.plus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )

                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )

                nextKey
            }
        }

        try {
            val response = remoteDataSource.getPointHistory(
                pg = page,
                row = pagingRequest.row,
                search_filter = pagingRequest.search_filter?.value,
                search_sdate = pagingRequest.search_sdate?.value,
                search_edate = pagingRequest.search_edate?.value,
                search_order = pagingRequest.search_order?.value,
            )

            if (response.isSuccessful) {
                response.body()?.let {
                    if (!it.isSuccess()) {
                        MediatorResult.Error(Exception(it.resultDetail))
                    }
                }
                val data =
                    response.body()?.data?.hisotry ?: return MediatorResult.Error(Exception())
                val endOfPaginationReached = data.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        localDataSource.deleteRemoteKeys(RemoteKeysType.POINT.name)
                        localDataSource.deleteAllPointHistory()
                    }

                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1

                    val keys = data.map {
                        RemoteKeys(
                            idx = it.guid ?: 0,
                            type = RemoteKeysType.POINT.name,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }

                    localDataSource.insertRemoteKeys(keys)
                    localDataSource.insertPointHistory(data.dtoToPointHistoryEntityList())
                }

                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                val message = when (response.code()) {
                    TokenErrorType.TypeUnprocessed.value -> {
                        "요청을 처리할 수 없습니다."
                    }

                    TokenErrorType.TypeRequestForbidden.value -> {
                        "사용자 인증 중 오류가 발생하였습니다."
                    }

                    TokenErrorType.TypeRequestMethodNotAllowed.value -> {
                        "해당 인증은 허용되지 않습니다."
                    }

                    TokenErrorType.TypeExpired.value -> {
                        errorCallback.postTokenExpiration()
                        "토큰이 만료되거나 토큰 형식이 맞지 않습니다."
                    }

                    else -> {
                        response.message()
                    }
                }

                return MediatorResult.Error(Exception(message))
            }
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, PointHistoryEntity>
    ): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let {
            localDataSource.getRemoteKeys(RemoteKeysType.POINT.name, it.guid)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, PointHistoryEntity>
    ): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let {
            localDataSource.getRemoteKeys(RemoteKeysType.POINT.name, it.guid)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PointHistoryEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.guid?.let {
                localDataSource.getRemoteKeys(RemoteKeysType.POINT.name, it)
            }
        }
    }
}
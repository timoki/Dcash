package com.dmonster.data.repository.repositoryImpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dmonster.data.local.database.MyDatabase
import com.dmonster.data.remote.dto.request.PagingRequest
import com.dmonster.data.repository.datasource.LocalDataSource
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.repository.mediator.PointHistoryListMediator
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.data.utils.ObjectMapper.toModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.paging.point.PointHistoryModel
import com.dmonster.domain.model.paging.point.PointModel
import com.dmonster.domain.repository.PointRepository
import com.dmonster.domain.type.TokenErrorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PointRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val database: MyDatabase,
    private val errorCallback: ErrorCallback,
) : PointRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPointHistory(
        row: Int,
        search_filter: StateFlow<String?>?,
        search_sdate: StateFlow<String?>?,
        search_edate: StateFlow<String?>?,
        search_order: StateFlow<String?>?,
    ): Flow<PagingData<PointHistoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = row,
                prefetchDistance = 2,
                initialLoadSize = 1
            ), remoteMediator = PointHistoryListMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource,
                database = database,
                errorCallback = errorCallback,
                pagingRequest = PagingRequest(
                    row = row,
                    search_filter = search_filter,
                    search_sdate = search_sdate,
                    search_edate = search_edate,
                    search_order = search_order,
                )
            )
        ) {
            localDataSource.getPointHistoryList()
        }.flow.map {
            it.map { entity ->
                entity.toModel()
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getUserPoint(): Flow<Result<PointModel>> = flow {
        val response = remoteDataSource.getUserPoint()

        if (response.isSuccessful) {
            response.body()?.let {
                if (!it.isSuccess()) {
                    it.statusCode?.let { statusCode ->
                        emit(Result.Error(
                            when (statusCode) {
                                TokenErrorType.TypeUnprocessed.value -> {
                                    errorCallback.postErrorData(it)
                                    "요청을 처리할 수 없습니다."
                                }

                                TokenErrorType.TypeRequestForbidden.value -> {
                                    errorCallback.postErrorData(it)
                                    "사용자 인증 중 오류가 발생하였습니다."
                                }

                                TokenErrorType.TypeRequestMethodNotAllowed.value -> {
                                    errorCallback.postErrorData(it)
                                    "해당 인증은 허용되지 않습니다."
                                }

                                TokenErrorType.TypeExpired.value -> {
                                    errorCallback.postTokenExpiration()
                                    "토큰이 만료되거나 토큰 형식이 맞지 않습니다."
                                }

                                else -> {
                                    errorCallback.postErrorData(it)
                                    it.resultDetail
                                }
                            }
                        ))

                        return@flow
                    }

                    emit(Result.Error(it.resultDetail))

                    return@flow
                }

                it.data?.let { data ->
                    emit(Result.Success(data.toModel()))
                }
            } ?: kotlin.run {
                throw Exception()
            }
        } else {
            emit(Result.NetworkError("네트워크 통신이 원활 하지 않습니다. 잠시 후 다시 시도해 주세요."))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(Dispatchers.IO)

}
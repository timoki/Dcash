package com.dmonster.data.repository.repositoryImpl

import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.data.utils.ObjectMapper.toModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.home.HomeDataModel
import com.dmonster.domain.repository.HomeRepository
import com.dmonster.domain.type.TokenErrorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val errorCallback: ErrorCallback,
) : HomeRepository {
    override fun getHomeData(): Flow<Result<HomeDataModel>> = flow {
        val response = remoteDataSource.getHomeData()

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
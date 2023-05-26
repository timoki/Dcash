package com.dmonster.data.repository.repositoryImpl

import android.util.Log
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.data.utils.ObjectMapper.toModel
import com.dmonster.domain.model.MemberInfoModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.repository.MemberRepository
import com.dmonster.domain.type.TokenErrorType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MemberRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val errorCallback: ErrorCallback,
) : MemberRepository {
    override fun requestLogin(
        id: String, pw: String
    ): Flow<Result<TokenModel>> = flow {
        val response = remoteDataSource.requestLogin(id, pw)

        if (response.isSuccessful) {
            response.body()?.let {
                if (it.isSuccess()) {
                    emit(Result.Success(it.data?.toModel()))

                    return@let
                }

                emit(Result.Error(it.resultDetail))
                errorCallback.postErrorData(it)
            } ?: kotlin.run {
                throw Exception()
            }
        } else {
            response.body()?.statusCode?.let {
                if (it == 401) {
                    errorCallback.postTokenExpiration()
                }
            }
            emit(Result.NetworkError("네트워크 통신이 원활 하지 않습니다. 잠시 후 다시 시도해 주세요."))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(Dispatchers.IO)

    override fun getMemberInfo(): Flow<Result<MemberInfoModel>> = flow {
        val response = remoteDataSource.getMemberInfo()

        if (response.isSuccessful) {
            response.body()?.data?.let {
                if (!it.isSuccess()) {
                    errorCallback.postErrorData(it)
                }

                emit(Result.Success(it.toModel()))
            } ?: kotlin.run {
                throw Exception()
            }
        } else {
            Log.d("아외안되", "${response.body()} / ${response.body()?.statusCode}")
            response.body()?.statusCode?.let {
                when (it) {
                    TokenErrorType.TypeUnprocessed.value -> {
                        emit(Result.Error("요청을 처리할 수 없습니다."))
                    }

                    TokenErrorType.TypeExpired.value -> {
                        errorCallback.postTokenExpiration()
                        emit(Result.Error("토큰이 만료되거나 토큰 형식이 맞지 않습니다."))
                    }

                    TokenErrorType.TypeRequestForbidden.value -> {
                        emit(Result.Error("사용자 인증 중 오류가 발생하였습니다."))
                    }

                    TokenErrorType.TypeRequestMethodNotAllowed.value -> {
                        emit(Result.Error("해당 인증은 허용되지 않습니다."))
                    }
                }

                return@flow
            }
            emit(Result.NetworkError("네트워크 통신이 원활 하지 않습니다. 잠시 후 다시 시도해 주세요."))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(Dispatchers.IO)
}
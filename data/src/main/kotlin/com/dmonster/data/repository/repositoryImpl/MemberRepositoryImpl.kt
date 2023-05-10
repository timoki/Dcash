package com.dmonster.data.repository.repositoryImpl

import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.data.utils.ObjectMapper.toModel
import com.dmonster.domain.model.MemberInfoModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.repository.MemberRepository
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
                if (!it.isSuccess()) {
                    errorCallback.postErrorData(it)
                }

                emit(Result.Success(it.toModel()))
            } ?: kotlin.run {
                throw Exception()
            }
        } else {
            emit(Result.NetworkError("네트워크 통신이 원활 하지 않습니다. 잠시 후 다시 시도해 주세요."))
        }
    }.catch {
        emit(Result.Error(it.message))
    }.flowOn(Dispatchers.IO)

    override fun getMemberInfo(): Flow<Result<MemberInfoModel>> = flow {
        val response = remoteDataSource.getMemberInfo()

        if (response.isSuccessful) {
            response.body()?.let {
                if (!it.isSuccess()) {
                    errorCallback.postErrorData(it)
                }

                emit(Result.Success(it.toModel()))
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
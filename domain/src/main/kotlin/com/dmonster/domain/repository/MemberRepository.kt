package com.dmonster.domain.repository

import com.dmonster.domain.model.MemberInfoModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import kotlinx.coroutines.flow.Flow

interface MemberRepository {
    fun requestLogin(
        id: String,
        pw: String
    ): Flow<Result<TokenModel>>

    fun getMemberInfo(): Flow<Result<MemberInfoModel>>
}
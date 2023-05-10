package com.dmonster.domain.repository

import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getAccessToken(
        refreshToken: String
    ): Flow<Result<TokenModel>>

    suspend fun changeRefreshToken(
        refreshToken: String
    ): Flow<Result<TokenModel>>
}
package com.dmonster.domain.usecase

import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    operator fun invoke(refreshToken: String): Flow<Result<TokenModel>> =
        tokenRepository.getAccessToken(refreshToken)
}
package com.dmonster.domain.usecase

import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangeRefreshTokenUseCase @Inject constructor(
    private val tokenRepository: TokenRepository
) {
    suspend operator fun invoke(refreshToken: String): Flow<Result<TokenModel>> =
        tokenRepository.changeRefreshToken(refreshToken)
}
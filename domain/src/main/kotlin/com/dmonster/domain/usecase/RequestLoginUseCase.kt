package com.dmonster.domain.usecase

import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestLoginUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(id: String, pw: String): Flow<Result<TokenModel>> =
        memberRepository.requestLogin(id, pw)
}
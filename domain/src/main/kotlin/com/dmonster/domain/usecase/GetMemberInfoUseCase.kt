package com.dmonster.domain.usecase

import com.dmonster.domain.model.MemberInfoModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.repository.MemberRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMemberInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    operator fun invoke(): Flow<Result<MemberInfoModel>> = memberRepository.getMemberInfo()
}
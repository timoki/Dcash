package com.dmonster.domain.usecase

import com.dmonster.domain.model.Result
import com.dmonster.domain.model.paging.point.PointModel
import com.dmonster.domain.repository.PointRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPointUseCase @Inject constructor(
    private val pointRepository: PointRepository
) {
    operator fun invoke(): Flow<Result<PointModel>> = pointRepository.getUserPoint()
}
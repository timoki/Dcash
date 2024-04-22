package com.dmonster.domain.usecase

import com.dmonster.domain.model.Result
import com.dmonster.domain.model.home.HomeDataModel
import com.dmonster.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeDataUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): Flow<Result<HomeDataModel>> =
        homeRepository.getHomeData()
}
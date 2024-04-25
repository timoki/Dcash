package com.dmonster.domain.usecase

import androidx.paging.PagingData
import com.dmonster.domain.model.paging.point.PointHistoryModel
import com.dmonster.domain.repository.PointRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetPointHistoryUseCase @Inject constructor(
    private val pointRepository: PointRepository
) {
    operator fun invoke(
        row: Int = 20,
        search_filter: StateFlow<String?>? = null,
        search_sdate: StateFlow<String?>? = null,
        search_edate: StateFlow<String?>? = null,
        search_order: StateFlow<String?>? = null,
    ): Flow<PagingData<PointHistoryModel>> = pointRepository.getPointHistory(
        row,
        search_filter,
        search_sdate,
        search_edate,
        search_order,
    )
}
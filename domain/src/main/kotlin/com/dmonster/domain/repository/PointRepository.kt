package com.dmonster.domain.repository

import androidx.paging.PagingData
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.paging.point.PointHistoryModel
import com.dmonster.domain.model.paging.point.PointModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PointRepository {
    fun getPointHistory(
        row: Int,
        search_filter: StateFlow<String?>?,
        search_sdate: StateFlow<String?>?,
        search_edate: StateFlow<String?>?,
        search_order: StateFlow<String?>?,
    ): Flow<PagingData<PointHistoryModel>>

    fun getUserPoint(): Flow<Result<PointModel>>
}
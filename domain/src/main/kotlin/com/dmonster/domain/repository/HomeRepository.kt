package com.dmonster.domain.repository

import com.dmonster.domain.model.Result
import com.dmonster.domain.model.home.HomeDataModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeData(): Flow<Result<HomeDataModel>>
}
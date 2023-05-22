package com.dmonster.data.repository.repositoryImpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dmonster.data.local.database.MyDatabase
import com.dmonster.data.repository.datasource.LocalDataSource
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.repository.mediator.NewsListMediator
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.data.utils.ObjectMapper.toModel
import com.dmonster.domain.model.NewsModel
import com.dmonster.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val database: MyDatabase,
    private val errorCallback: ErrorCallback,
) : NewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getNewsList(): Flow<PagingData<NewsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 10
            ),
            remoteMediator = NewsListMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource,
                database = database,
                errorCallback = errorCallback
            )
        ) {
            localDataSource.getNewsList()
        }.flow.map {
            it.map { entity ->
                entity.toModel()
            }
        }.flowOn(Dispatchers.IO)
    }
}
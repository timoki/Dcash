package com.dmonster.data.repository.repositoryImpl

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.dmonster.data.local.database.MyDatabase
import com.dmonster.data.remote.dto.request.NewsRequest
import com.dmonster.data.repository.datasource.LocalDataSource
import com.dmonster.data.repository.datasource.RemoteDataSource
import com.dmonster.data.repository.mediator.NewsListMediator
import com.dmonster.data.utils.ErrorCallback
import com.dmonster.data.utils.ObjectMapper.toModel
import com.dmonster.domain.model.paging.news.NewsListModel
import com.dmonster.domain.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
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
    override fun getNewsList(
        row: Int,
        search_filter: StateFlow<String?>?,
        search_value: StateFlow<String?>?,
        search_sdate: StateFlow<String?>?,
        search_edate: StateFlow<String?>?,
        search_order: StateFlow<String?>?,
        search_category: StateFlow<String?>?,
        search_author: StateFlow<String?>?,
        search_creator: StateFlow<String?>?,
    ): Flow<PagingData<NewsListModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = row,
                prefetchDistance = 2,
                initialLoadSize = 1
            ), remoteMediator = NewsListMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource,
                database = database,
                errorCallback = errorCallback,
                newsRequest = NewsRequest(
                    row,
                    search_filter,
                    search_value,
                    search_sdate,
                    search_edate,
                    search_order,
                    search_category,
                    search_author,
                    search_creator,
                )
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
package com.dmonster.domain.usecase

import com.dmonster.domain.model.Result
import com.dmonster.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestViewNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(guid: Long): Flow<Result<Boolean>> {
        return newsRepository.viewNews(guid)
    }
}
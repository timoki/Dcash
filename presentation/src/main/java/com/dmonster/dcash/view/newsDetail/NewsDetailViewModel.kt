package com.dmonster.dcash.view.newsDetail

import androidx.lifecycle.viewModelScope
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.usecase.RequestViewNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val dataStoreModule: DataStoreModule,
    private val viewNewsUseCase: RequestViewNewsUseCase,
) : BaseViewModel() {
    val isLoadNews = MutableStateFlow(false)

    val isTimeComplete = MutableStateFlow(false)
    val isScrollComplete = MutableStateFlow(false)

    val isCanGetPoint = combine(isTimeComplete, isScrollComplete) { time, scroll ->
        time && scroll
    }

    suspend fun isNotShowingNewsViewTutorial(): Boolean {
        return dataStoreModule.isNotShowingNewsViewTutorial.first()
    }

    private val _viewNews = MutableStateFlow(false)
    val viewNews = _viewNews.asStateFlow()

    fun requestViewNews(guid: Long) = viewModelScope.launch {
        viewNewsUseCase.invoke(guid).collect { result ->
            when (result) {
                is Result.Loading -> {
                    showLoadingDialog()
                }

                is Result.Success -> {
                    hideLoadingDialog()
                    _viewNews.emit(result.data ?: false)
                }

                is Result.Error -> {
                    hideLoadingDialog()
                }

                is Result.NetworkError -> {
                    hideLoadingDialog()
                }
            }
        }
    }
}
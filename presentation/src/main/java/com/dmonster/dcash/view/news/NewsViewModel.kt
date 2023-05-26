package com.dmonster.dcash.view.news

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.domain.model.paging.PageModel
import com.dmonster.domain.type.PagingLoadingType
import com.dmonster.domain.usecase.GetNewsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class NewsViewModel @Inject constructor(
    private val getNewsListUseCase: GetNewsListUseCase
) : BaseViewModel() {

    val loadingType = MutableStateFlow(PagingLoadingType.LOADING)

    val newsList = getNewsListUseCase().cachedIn(viewModelScope)

    private val _refreshList = Channel<Unit>(Channel.CONFLATED)
    val refreshList = _refreshList.receiveAsFlow()

    fun onRefreshList() = viewModelScope.launch {
        _refreshList.send(Unit)
    }
}
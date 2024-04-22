package com.dmonster.dcash.view.news

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.domain.type.PagingLoadingType
import com.dmonster.domain.usecase.GetNewsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsListUseCase: GetNewsListUseCase
) : BaseViewModel() {

    val loadingType = MutableStateFlow(PagingLoadingType.LOADING)

    var searchFilter = MutableStateFlow<String?>(null)
    val searchText = MutableStateFlow<String?>(null)
    var searchStartDate = MutableStateFlow<String?>(null)
    var searchEndDate = MutableStateFlow<String?>(null)
    var searchOrder = MutableStateFlow<String?>(null)
    val selectCategory = MutableStateFlow<String?>(null)
    val selectCreator = ArrayList<String>()
    val selectAuthor = ArrayList<String>()

    private fun splitString(list: ArrayList<String>): String? {
        var str: String? = null
        list.forEachIndexed { index, s ->
            if (index == 0) str = s
            else str += ",$s"
        }

        return str
    }

    val newsList = getNewsListUseCase(
        row = 20,
        search_filter = searchFilter.value,
        search_value = searchText.value,
        search_sdate = searchStartDate.value,
        search_edate = searchEndDate.value,
        search_order = searchOrder.value,
        search_category = selectCategory.value,
        search_author = splitString(selectAuthor),
        search_creator = splitString(selectCreator)
    ).cachedIn(viewModelScope)

    private val _refreshList = Channel<Unit>(Channel.CONFLATED)
    val refreshList = _refreshList.receiveAsFlow()

    fun onRefreshList() = viewModelScope.launch {
        _refreshList.send(Unit)
    }

    val isSelectFilter =
        MutableStateFlow(selectCreator.isNotEmpty() && selectAuthor.isNotEmpty())
}
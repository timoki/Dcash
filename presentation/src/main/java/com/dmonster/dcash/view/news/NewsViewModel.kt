package com.dmonster.dcash.view.news

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.dcash.view.dialog.select.adapter.SelectDialogData
import com.dmonster.domain.type.PagingLoadingType
import com.dmonster.domain.usecase.GetNewsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsListUseCase: GetNewsListUseCase
) : BaseViewModel() {

    val loadingType = MutableStateFlow(PagingLoadingType.LOADING)

    var selectOrder = MutableStateFlow(SelectDialogData("desc", "최신순", true))

    val searchFilter = MutableStateFlow<String?>(null)
    val searchValue = MutableStateFlow<String?>(null)
    val searchOrder = MutableStateFlow("desc")
    val searchCategory = MutableStateFlow<String?>(null)
    private val searchCreator = MutableStateFlow<String?>(null)
    private val searchAuthor = MutableStateFlow<String?>(null)

    fun setSearchCreator(creator: String, isSelected: Boolean) {
        searchCreator.value = replaceSelectFilterString(searchCreator.value, creator, isSelected)
    }

    fun setSearchAuthor(author: String, isSelected: Boolean) {
        searchAuthor.value = replaceSelectFilterString(searchAuthor.value, author, isSelected)
    }

    private fun replaceSelectFilterString(
        oldSelect: String?,
        str: String,
        isSelected: Boolean
    ): String? {
        var oldString = oldSelect
        if (isSelected) {
            if (oldString == null) oldString = str
            else oldString += ",$str"
        } else {
            oldString = if (oldString.equals(str)) {
                null
            } else if (oldString?.indexOf(str) == 0) {
                oldString.replace("$str,", "")
            } else {
                oldString?.replace(",$str", "")
            }
        }

        return oldString
    }

    val newsList = getNewsListUseCase(
        row = 20,
        search_filter = searchFilter,
        search_value = searchValue,
        search_order = searchOrder,
        search_category = searchCategory,
        search_author = searchAuthor,
        search_creator = searchCreator,
    ).cachedIn(viewModelScope)

    private val _onResetFilterChannel = Channel<Unit>(Channel.CONFLATED)
    val onResetFilterChannel = _onResetFilterChannel.receiveAsFlow()

    fun onResetFilter() = viewModelScope.launch {
        selectOrder.value = SelectDialogData("desc", "최신순", true)
        searchFilter.value = null
        searchValue.value = null
        searchOrder.value = "desc"
        searchCategory.value = null
        searchCreator.value = null
        searchAuthor.value = null

        _onResetFilterChannel.send(Unit)
    }

    val isSelectFilter = combine(searchCreator, searchAuthor) { creator, author ->
        creator != null || author != null
    }

    val isSearchFilter = combine(searchFilter, searchValue) { filter, value ->
        filter != null || value != null
    }

    val isFilter = combine(isSelectFilter, isSearchFilter) { select, search ->
        select || search
    }

    private val _sortingClickChannel = Channel<Unit>(Channel.CONFLATED)
    val sortingClickChannel = _sortingClickChannel.receiveAsFlow()

    fun sortingClick() = viewModelScope.launch {
        _sortingClickChannel.send(Unit)
    }
}
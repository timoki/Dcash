package com.dmonster.dcash.view.search

import androidx.lifecycle.viewModelScope
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.dcash.view.dialog.select.adapter.SelectDialogData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val dataStore: DataStoreModule
) : BaseViewModel() {

    val searchText = MutableStateFlow("")
    var selectType = MutableStateFlow(SelectDialogData("all", "통합검색", true))
    var selectTypeText = MutableStateFlow("통합검색")

    private val _searchTypeClickChannel = Channel<Unit>(Channel.CONFLATED)
    val searchTypeClickChannel = _searchTypeClickChannel.receiveAsFlow()

    fun onSearchTypeClick() = viewModelScope.launch {
        _searchTypeClickChannel.send(Unit)
    }
}
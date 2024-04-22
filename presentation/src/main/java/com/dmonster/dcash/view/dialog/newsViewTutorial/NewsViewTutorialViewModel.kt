package com.dmonster.dcash.view.dialog.newsViewTutorial

import androidx.lifecycle.viewModelScope
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.dcash.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewTutorialViewModel @Inject constructor(
    private val dataStore: DataStoreModule
) : BaseViewModel() {
    val isCheck = MutableStateFlow(false)

    private val _onClickChannel = Channel<Unit>(Channel.CONFLATED)
    val onClickChannel = _onClickChannel.receiveAsFlow()

    fun onClick() = viewModelScope.launch {
        dataStore.putNotShowingNewsViewTutorial(isCheck.value)
        _onClickChannel.send(Unit)
    }
}
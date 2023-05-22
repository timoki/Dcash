package com.dmonster.dcash.view.home

import androidx.lifecycle.viewModelScope
import com.dmonster.dcash.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class HomeViewModel @Inject constructor(

) : BaseViewModel() {
    private val _onClickChannel = Channel<Unit>(Channel.CONFLATED)
    val onClickChannel = _onClickChannel.receiveAsFlow()

    fun onClick() = viewModelScope.launch {
        _onClickChannel.send(Unit)
    }
}
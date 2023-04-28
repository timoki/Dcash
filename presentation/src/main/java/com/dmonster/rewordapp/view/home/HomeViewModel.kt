package com.dmonster.rewordapp.view.home

import androidx.lifecycle.viewModelScope
import com.dmonster.rewordapp.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(

) : BaseViewModel() {
    private val _onClickChannel = Channel<Unit>(Channel.CONFLATED)
    val onClickChannel = _onClickChannel.receiveAsFlow()

    fun onClick() = viewModelScope.launch {
        _onClickChannel.send(Unit)
    }
}
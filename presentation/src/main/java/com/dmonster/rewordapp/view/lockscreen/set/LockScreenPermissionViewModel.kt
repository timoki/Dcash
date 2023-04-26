package com.dmonster.rewordapp.view.lockscreen.set

import androidx.lifecycle.viewModelScope
import com.dmonster.rewordapp.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LockScreenPermissionViewModel : BaseViewModel() {
    private val _onClickChannel = Channel<Unit>(Channel.CONFLATED)
    val onClickChannel = _onClickChannel.receiveAsFlow()

    fun onClick() = viewModelScope.launch {
        _onClickChannel.send(Unit)
    }
}
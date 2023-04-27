package com.dmonster.rewordapp.view.lockscreen

import androidx.lifecycle.viewModelScope
import com.dmonster.rewordapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockScreenViewModel @Inject constructor(

): BaseViewModel() {

    private val _lockOffChannel = Channel<Unit>(Channel.CONFLATED)
    val lockOffChannel = _lockOffChannel.receiveAsFlow()

    fun lockOff() = viewModelScope.launch {
        _lockOffChannel.send(Unit)
    }
}
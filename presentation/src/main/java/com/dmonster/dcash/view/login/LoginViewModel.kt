package com.dmonster.dcash.view.login

import androidx.lifecycle.viewModelScope
import com.dmonster.dcash.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : BaseViewModel() {

    private val _loginClickChannel = Channel<Unit>(Channel.CONFLATED)
    val loginClickChannel = _loginClickChannel.receiveAsFlow()

    fun loginClick() = viewModelScope.launch {
        _loginClickChannel.send(Unit)
    }
}
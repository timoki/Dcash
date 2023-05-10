package com.dmonster.dcash.view.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.usecase.RequestLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val requestLoginUseCase: RequestLoginUseCase
) : BaseViewModel() {

    private val _loginClickChannel = Channel<Unit>(Channel.CONFLATED)
    val loginClickChannel = _loginClickChannel.receiveAsFlow()

    fun loginClick() = viewModelScope.launch {
        requestLoginUseCase.invoke("test", "1016").onEach { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("아외안되", "Loading")
                }

                is Result.Success -> {
                    Log.d("아외안되", "Loading / ${result.data}")
                }

                is Result.Error -> {
                    Log.d("아외안되", "Error / ${result.message}")
                }

                is Result.NetworkError -> {
                    Log.d("아외안되", "NetworkError / ${result.message}")
                }
            }
        }
        //_loginClickChannel.send(Unit)
    }
}
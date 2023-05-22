package com.dmonster.dcash.view.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.dcash.utils.StaticData
import com.dmonster.dcash.utils.observeInLifecycleStop
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.type.NavigateType
import com.dmonster.domain.usecase.GetMemberInfoUseCase
import com.dmonster.domain.usecase.RequestLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val requestLoginUseCase: RequestLoginUseCase
) : BaseViewModel() {

    private val _loginSuccessChannel = Channel<Unit>(Channel.CONFLATED)
    val loginSuccessChannel = _loginSuccessChannel.receiveAsFlow()

    private fun login(): StateFlow<Result<TokenModel>> =
        requestLoginUseCase.invoke("test", "1016")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.Loading()
            )

    fun loginClick() = viewModelScope.launch {
        login().collect { result ->
            when (result) {
                is Result.Loading -> {
                    showLoadingDialog()
                }

                is Result.Success -> {
                    hideLoadingDialog()
                    result.data?.let {
                        StaticData.tokenData.value = it
                        _loginSuccessChannel.send(Unit)
                    }
                }

                is Result.Error -> {
                    hideLoadingDialog()
                }

                is Result.NetworkError -> {
                    hideLoadingDialog()
                }
            }
        }
    }
}
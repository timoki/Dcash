package com.dmonster.dcash.view.login

import androidx.lifecycle.viewModelScope
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.dcash.utils.StaticData
import com.dmonster.domain.model.Result
import com.dmonster.domain.type.SnsType
import com.dmonster.domain.usecase.RequestLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val requestLoginUseCase: RequestLoginUseCase,
    private val dataStore: DataStoreModule
) : BaseViewModel() {

    val loginIdText = MutableStateFlow("")
    val loginPwText = MutableStateFlow("")

    val loginIdError = MutableStateFlow("")
    val loginPwError = MutableStateFlow("")

    private val _loginSuccessChannel = Channel<Unit>(Channel.CONFLATED)
    val loginSuccessChannel = _loginSuccessChannel.receiveAsFlow()

    fun autoLoginCheck() = viewModelScope.launch {
        dataStore.run {
            if (getLoginId.first().isNotEmpty()) {
                loginIdText.value = getLoginId.first()
                loginPwText.value = getLoginPw.first()
                login()
            }
        }
    }

    fun login() = viewModelScope.launch {
        requestLoginUseCase.invoke(loginIdText.value, loginPwText.value)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.Loading()
            ).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoadingDialog()
                    }

                    is Result.Success -> {
                        hideLoadingDialog()
                        result.data?.let {
                            StaticData.tokenData.value = it
                            dataStore.putLoginId(loginIdText.value)
                            dataStore.putLoginPw(loginPwText.value)
                            _loginSuccessChannel.send(Unit)
                        }
                    }

                    is Result.Error -> {
                        hideLoadingDialog()
                        loginIdError.value = result.message ?: ""
                    }

                    is Result.NetworkError -> {
                        hideLoadingDialog()
                        loginIdError.value = result.message ?: ""
                    }
                }
            }
    }

    private val _loginClickChannel = Channel<Unit>(Channel.CONFLATED)
    val loginClickChannel = _loginClickChannel.receiveAsFlow()

    fun loginClick() = viewModelScope.launch {
        _loginClickChannel.send(Unit)
    }

    fun joinClick() = viewModelScope.launch {

    }

    fun findAccountClick() = viewModelScope.launch {

    }

    fun snsClick(type: SnsType) = viewModelScope.launch {

    }
}
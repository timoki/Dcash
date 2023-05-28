package com.dmonster.dcash.view.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.dcash.utils.StaticData
import com.dmonster.dcash.utils.showSnackBar
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.type.SnsType
import com.dmonster.domain.usecase.RequestLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val requestLoginUseCase: RequestLoginUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    val loginIdText = MutableStateFlow("")
    val loginPwText = MutableStateFlow("")

    private val _loginIdError = Channel<String>(Channel.CONFLATED)
    val loginIdError = _loginIdError.receiveAsFlow()

    private val _loginPwError = Channel<String>(Channel.CONFLATED)
    val loginPwError = _loginPwError.receiveAsFlow()

    private val _loginSuccessChannel = Channel<Unit>(Channel.CONFLATED)
    val loginSuccessChannel = _loginSuccessChannel.receiveAsFlow()

    private fun login(): StateFlow<Result<TokenModel>> =
        requestLoginUseCase.invoke(loginIdText.value, loginPwText.value)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Result.Loading()
            )

    fun loginClick() = viewModelScope.launch {
        if (loginIdText.value.isNullOrEmpty()) {
            _loginIdError.send(context.getString(R.string.error_login_id_empty))

            return@launch
        }

        if (loginPwText.value.isNullOrEmpty()) {
            _loginPwError.send(context.getString(R.string.error_login_pw_empty))

            return@launch
        }

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

    fun joinClick() = viewModelScope.launch {

    }

    fun findAccountClick() = viewModelScope.launch {

    }

    fun snsClick(type: SnsType) = viewModelScope.launch {

    }
}
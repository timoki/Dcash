package com.dmonster.dcash.view.dialog.getPoint

import androidx.lifecycle.viewModelScope
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.usecase.GetUserPointUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetPointViewModel @Inject constructor(
    private val getUserPointUseCase: GetUserPointUseCase
) : BaseViewModel() {

    init {
        getUserPoint()
    }

    private val _onClickChannel = Channel<Unit>(Channel.CONFLATED)
    val onClickChannel = _onClickChannel.receiveAsFlow()

    fun onClick() = viewModelScope.launch {
        _onClickChannel.send(Unit)
    }

    val userPoint = MutableStateFlow(0)

    private fun getUserPoint() = viewModelScope.launch {
        getUserPointUseCase.invoke().stateIn(
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
                    userPoint.value = result.data?.point ?: 0
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
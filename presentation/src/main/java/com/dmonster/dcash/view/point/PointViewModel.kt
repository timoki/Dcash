package com.dmonster.dcash.view.point

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.usecase.GetPointHistoryUseCase
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
class PointViewModel @Inject constructor(
    private val userPointUseCase: GetUserPointUseCase,
    private val pointHistoryUseCase: GetPointHistoryUseCase,
) : BaseViewModel() {

    init {
        getUserPoint()
    }

    val userPoint = MutableStateFlow(0)

    private fun getUserPoint() = viewModelScope.launch {
        userPointUseCase.invoke().stateIn(
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

    val pointList = pointHistoryUseCase.invoke(
        row = 20
    ).cachedIn(viewModelScope)

    private val _onUsePointClickChannel = Channel<Unit>(Channel.CONFLATED)
    val onUsePointClickChannel = _onUsePointClickChannel.receiveAsFlow()

    fun onUsePointClick() = viewModelScope.launch {
        _onUsePointClickChannel.send(Unit)
    }
}
package com.dmonster.dcash.view.home

import androidx.lifecycle.viewModelScope
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.home.HomeDataModel
import com.dmonster.domain.usecase.GetHomeDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeDataUseCase: GetHomeDataUseCase
) : BaseViewModel() {

    init {
        getHomeData()
    }

    private val _getHomeDataErrorChannel = Channel<String?>(Channel.CONFLATED)
    val getHomeDataErrorChannel = _getHomeDataErrorChannel.receiveAsFlow()

    private val _moreRecommendClickChannel = Channel<Unit>(Channel.CONFLATED)
    val moreRecommendClickChannel = _moreRecommendClickChannel.receiveAsFlow()

    val moreRecommend = MutableStateFlow(false)

    fun moreRecommendClick() = viewModelScope.launch {
        _moreRecommendClickChannel.send(Unit)
    }

    private val _allNewsClickChannel = Channel<Unit>(Channel.CONFLATED)
    val allNewsClickChannel = _allNewsClickChannel.receiveAsFlow()

    fun allNewsClick() = viewModelScope.launch {
        _allNewsClickChannel.send(Unit)
    }

    val homeData = MutableStateFlow<HomeDataModel?>(null)

    private fun requestHomeData() = homeDataUseCase.invoke().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading()
        )

    fun getHomeData() = viewModelScope.launch {
        requestHomeData().collectLatest { result ->
            when (result) {
                is Result.Loading -> {
                    showLoadingDialog()
                }

                is Result.Success -> {
                    hideLoadingDialog()
                    homeData.value = result.data
                    moreRecommend.value = false
                }

                is Result.Error -> {
                    hideLoadingDialog()
                    _getHomeDataErrorChannel.send(result.message)
                }

                is Result.NetworkError -> {
                    hideLoadingDialog()
                    _getHomeDataErrorChannel.send(result.message)
                }
            }
        }
    }
}
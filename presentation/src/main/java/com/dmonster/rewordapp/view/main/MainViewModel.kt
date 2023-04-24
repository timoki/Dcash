package com.dmonster.rewordapp.view.main

import androidx.lifecycle.viewModelScope
import com.dmonster.rewordapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.timoky.domain.model.navi.NavigateType
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : BaseViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun showLoadingDialog() = viewModelScope.launch {
        _isLoading.emit(true)
    }

    fun hideLoadingDialog() = viewModelScope.launch {
        _isLoading.emit(false)
    }

    private val _navigateToChannel =
        MutableStateFlow<Pair<NavigateType, Boolean>?>(null)
    val navigateToChannel = _navigateToChannel.asStateFlow()

    fun fragmentNavigateTo(item: NavigateType, isCloseFragment: Boolean = false) =
        viewModelScope.launch {
            _navigateToChannel.value = item to isCloseFragment
        }

    val isBottomAppBarVisible = MutableStateFlow(false)
}
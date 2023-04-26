package com.dmonster.rewordapp.view.main

import androidx.lifecycle.viewModelScope
import com.dmonster.domain.type.NavigateType
import com.dmonster.rewordapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(

) : BaseViewModel() {

    val isTopViewVisible = MutableStateFlow(false)
    val isBottomAppBarVisible = MutableStateFlow(false)

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun showLoadingDialog() = viewModelScope.launch {
        _isLoading.emit(true)
    }

    fun hideLoadingDialog() = viewModelScope.launch {
        _isLoading.emit(false)
    }

    private val _navigateToChannel = MutableStateFlow<Pair<NavigateType, Boolean>?>(null)
    val navigateToChannel = _navigateToChannel.asStateFlow()

    fun fragmentNavigateTo(item: NavigateType, isCloseFragment: Boolean = false) =
        viewModelScope.launch {
            _navigateToChannel.value = item to isCloseFragment
        }

    private val _setOverlayPermissionChannel = Channel<Unit>(Channel.CONFLATED)
    val setOverlayPermissionChannel = _setOverlayPermissionChannel.receiveAsFlow()

    fun setOverlayPermission() = viewModelScope.launch {
        _setOverlayPermissionChannel.send(Unit)
    }

    fun onBottomMenuClick(type: Int) {
        val item = when (type) {
            0 -> NavigateType.Home()
            1 -> NavigateType.News()
            2 -> NavigateType.Event()
            3 -> NavigateType.Point()
            4 -> NavigateType.MyPage()
            else -> NavigateType.Home()
        }
        fragmentNavigateTo(item)
    }

    private val _checkPermissionChannel = Channel<Int>(Channel.CONFLATED)
    val checkPermissionChannel = _checkPermissionChannel.receiveAsFlow()

    fun checkPermission(type: Int) = viewModelScope.launch {
        _checkPermissionChannel.send(type)
    }
}
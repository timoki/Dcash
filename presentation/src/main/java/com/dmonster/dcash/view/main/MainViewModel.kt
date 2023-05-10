package com.dmonster.dcash.view.main

import androidx.lifecycle.viewModelScope
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.domain.type.NavigateType
import com.dmonster.dcash.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStoreModule
) : BaseViewModel() {

    var isShowLockScreenPopup = false

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

    private val _navigateToChannel = MutableStateFlow<NavigateType?>(null)
    val navigateToChannel = _navigateToChannel.asStateFlow()

    fun fragmentNavigateTo(item: NavigateType?) = viewModelScope.launch {
        item?.let {
            _navigateToChannel.value = it
        }
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

    private val _goPermissionSettingChannel = Channel<Unit>(Channel.CONFLATED)
    val goPermissionSettingChannel = _goPermissionSettingChannel.receiveAsFlow()

    fun goPermissionSetting() = viewModelScope.launch {
        _goPermissionSettingChannel.send(Unit)
    }

    val isUseLockScreen = MutableStateFlow<Boolean>(false)

    fun getUseLockScreen() = viewModelScope.launch {
        isUseLockScreen.value = dataStore.getPreferencesData(DataStoreModule.USE_LOCK_SCREEN).first()
    }

    fun setUseLockScreen(isUse: Boolean) = viewModelScope.launch {
        dataStore.putPreferencesData(DataStoreModule.USE_LOCK_SCREEN, isUse)
    }

    var accessToken = ""

    fun getAccessToken() = viewModelScope.launch {
        accessToken = dataStore.getPreferencesData(DataStoreModule.ACCESS_TOKEN).first()
    }
}
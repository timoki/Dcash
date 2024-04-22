package com.dmonster.dcash.view.main

import androidx.lifecycle.viewModelScope
import com.dmonster.data.local.datastore.DataStoreModule
import com.dmonster.dcash.base.BaseViewModel
import com.dmonster.domain.model.MemberInfoModel
import com.dmonster.domain.model.Result
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.model.home.FilterModel
import com.dmonster.domain.usecase.GetAccessTokenUseCase
import com.dmonster.domain.usecase.GetMemberInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStore: DataStoreModule,
    private val getMemberInfoUseCase: GetMemberInfoUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : BaseViewModel() {

    val userInfo = MutableStateFlow<MemberInfoModel?>(null)

    var isShowLockScreenPopup = false

    val isTopViewVisible = MutableStateFlow(false)
    val isBottomAppBarVisible = MutableStateFlow(false)

    private val _setOverlayPermissionChannel = Channel<Unit>(Channel.CONFLATED)
    val setOverlayPermissionChannel = _setOverlayPermissionChannel.receiveAsFlow()

    fun setOverlayPermission() = viewModelScope.launch {
        _setOverlayPermissionChannel.send(Unit)
    }

    val currentPageIndex = MutableStateFlow(3)

    private val _onBottomMenuClickChannel = Channel<Int>(Channel.CONFLATED)
    val onBottomMenuClickChannel = _onBottomMenuClickChannel.receiveAsFlow()

    fun onBottomMenuClick(type: Int) = viewModelScope.launch {
        currentPageIndex.value = type
        _onBottomMenuClickChannel.send(type)
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

    val isUseLockScreen = MutableStateFlow(false)

    fun getUseLockScreen() = viewModelScope.launch {
        isUseLockScreen.value = dataStore.isUseLockScreen.first()
    }

    fun setUseLockScreen(isUse: Boolean) = viewModelScope.launch {
        dataStore.putUseLockScreen(isUse)
    }

    fun getMemberInfo(): StateFlow<Result<MemberInfoModel>> = getMemberInfoUseCase.invoke().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading()
    )

    fun getAccessToken(refreshToken: String): StateFlow<Result<TokenModel>> =
        getAccessTokenUseCase.invoke(refreshToken).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading()
        )

    private val _scrollTop = Channel<Unit>(Channel.CONFLATED)
    val scrollTop = _scrollTop.receiveAsFlow()

    fun onScrollTop() = viewModelScope.launch {
        _scrollTop.send(Unit)
    }

    val topButtonVisible = MutableStateFlow(false)

    val newsCategory = MutableStateFlow<List<FilterModel>?>(null)
    val newsAuthor = MutableStateFlow<List<FilterModel>?>(null)
    val newsCreator = MutableStateFlow<List<FilterModel>?>(null)
}
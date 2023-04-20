package com.dmonster.rewordapp.view.main

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.dmonster.rewordapp.NavigationDirections
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

    private val _navigateToChannel = MutableStateFlow<Pair<NavigateType, NavDirections>?>(null)
    val navigateToChannel = _navigateToChannel.asStateFlow()

    fun fragmentNavigateTo(item: NavigateType) = viewModelScope.launch {
        _navigateToChannel.value = item to
                when (item) {
                    is NavigateType.Login -> NavigationDirections.actionGlobalLoginFragment()
                }
    }
}
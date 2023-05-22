package com.dmonster.dcash.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun showLoadingDialog() = viewModelScope.launch {
        _isLoading.emit(true)
    }

    fun hideLoadingDialog() = viewModelScope.launch{
        _isLoading.emit(false)
    }
}
package com.dmonster.rewordapp.view.intro

import com.dmonster.rewordapp.R
import com.dmonster.rewordapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(

): BaseViewModel() {

    private val _isLottieUse = MutableStateFlow(true)
    val isLottieUse = _isLottieUse.asStateFlow()

    private val _lottieImage = MutableStateFlow(R.raw.splash_test)
    val lottieImage = _lottieImage.asStateFlow()

    private val _splashImage = MutableStateFlow(R.drawable.splash)
    val splashImage = _splashImage.asStateFlow()

    fun requestGetConfigData() {

    }
}
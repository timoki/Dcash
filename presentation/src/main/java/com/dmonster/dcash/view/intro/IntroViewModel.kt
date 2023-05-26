package com.dmonster.dcash.view.intro

import com.dmonster.dcash.R
import com.dmonster.dcash.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
internal class IntroViewModel @Inject constructor(

): BaseViewModel() {

    private val _isLottieUse = MutableStateFlow(false)
    val isLottieUse = _isLottieUse.asStateFlow()

    private val _lottieImage = MutableStateFlow(R.raw.splash_test)
    val lottieImage = _lottieImage.asStateFlow()

    private val _splashImage = MutableStateFlow(R.drawable.intro)
    val splashImage = _splashImage.asStateFlow()

    fun requestGetConfigData() {

    }
}
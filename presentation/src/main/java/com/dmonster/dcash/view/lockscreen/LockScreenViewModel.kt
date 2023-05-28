package com.dmonster.dcash.view.lockscreen

import com.dmonster.dcash.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class LockScreenViewModel @Inject constructor(

): BaseViewModel() {
    val isThereAnyNewsToRead = MutableStateFlow(false)
}
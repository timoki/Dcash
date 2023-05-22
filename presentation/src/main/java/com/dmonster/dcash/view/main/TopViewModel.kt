package com.dmonster.dcash.view.main

import androidx.lifecycle.viewModelScope
import com.dmonster.domain.type.TopMenuType
import com.dmonster.dcash.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TopViewModel @Inject constructor(

) : BaseViewModel() {

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    fun getTitleText(): String {
        return title.value
    }

    fun setTitleText(titleText: String) {
        setTopMenu(titleText = titleText)
    }

    private val _leftMenu = MutableStateFlow(TopMenuType.LeftMenu.NONE)
    val leftMenu = _leftMenu.asStateFlow()

    fun getLeftMenuMode(): Int {
        return leftMenu.value
    }

    fun setLeftMenuMode(menu: Int) {
        setTopMenu(leftMenu = menu)
    }

    private val _middleMenu = MutableStateFlow(TopMenuType.MiddleMenu.NONE)
    val middleMenu = _middleMenu.asStateFlow()

    fun getMiddleMenuMode(): Int {
        return middleMenu.value
    }

    fun setMiddleMenuMode(menu: Int) {
        setTopMenu(middleMenu = menu)
    }

    private val _rightMenu = MutableStateFlow(TopMenuType.RightMenu.NONE)
    val rightMenu = _rightMenu.asStateFlow()

    fun getRightMenuMode(): Int {
        return rightMenu.value
    }

    fun setRightMenuMode(menu: Int) {
        setTopMenu(rightMenu = menu)
    }

    fun setTopMenu(
        leftMenu: Int = getLeftMenuMode(),
        middleMenu: Int = getMiddleMenuMode(),
        rightMenu: Int = getRightMenuMode(),
        titleText: String = getTitleText()
    ) = viewModelScope.launch {
        _leftMenu.value = leftMenu
        _middleMenu.value = middleMenu
        _rightMenu.value = rightMenu
        _title.value = titleText
    }

    /**
    * 클릭 리스너
    * */

    private val _onBackClickChannel = Channel<Unit>(Channel.CONFLATED)
    val onBackClickChannel = _onBackClickChannel.receiveAsFlow()

    fun onBackClick() = viewModelScope.launch {
        _onBackClickChannel.send(Unit)
    }

    private val _onRightMenuClickChannel = Channel<Int>(Channel.CONFLATED)
    val onRightMenuClickChannel = _onRightMenuClickChannel.receiveAsFlow()

    fun onRightMenuClick() = viewModelScope.launch {
        _onRightMenuClickChannel.send(getRightMenuMode())
    }

    private val _onRightMenuSubClickChannel = Channel<Int>(Channel.CONFLATED)
    val onRightMenuSubClickChannel = _onRightMenuSubClickChannel.receiveAsFlow()

    fun onRightMenuSubClick() = viewModelScope.launch {
        _onRightMenuSubClickChannel.send(getRightMenuMode())
    }
}
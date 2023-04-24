package com.dmonster.rewordapp.view.dialog

import com.dmonster.rewordapp.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class BasicDialogViewModel @Inject constructor(

) : BaseViewModel() {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    fun getTitleText(): String = title.value

    fun setTitleText(titleText: String) {
        _title.value = titleText
    }

    private val _subject = MutableStateFlow("")
    val subject = _subject.asStateFlow()

    fun getSubjectText(): String = subject.value

    fun setSubjectText(text: String) {
        _subject.value = text
    }

    private val _checkBoxSetting = MutableStateFlow(false to "")
    val checkBoxSetting = _checkBoxSetting.asStateFlow()

    val isCheckBoxVisible: Boolean
        get() = checkBoxSetting.value.first

    val checkBoxText: String
        get() = checkBoxSetting.value.second

    fun setCheckBoxVisible(bool: Boolean) {
        _checkBoxSetting.value = bool to checkBoxText
    }

    fun setCheckBoxText(text: String) {
        _checkBoxSetting.value = isCheckBoxVisible to text
    }

    fun setCheckBox(bool: Boolean, text: String) {
        _checkBoxSetting.value = bool to text
    }

    private val _negativeButtonSetting = MutableStateFlow(Triple(
        false, ""
    ) {})
    val negativeButtonSetting = _negativeButtonSetting.asStateFlow()

    val isNegativeButtonVisible: Boolean
        get() = negativeButtonSetting.value.first

    val negativeButtonText: String
        get() = negativeButtonSetting.value.second

    val negativeButtonClickListener: () -> Unit
        get() = negativeButtonSetting.value.third

    fun setNegativeButtonVisible(bool: Boolean) {
        _negativeButtonSetting.value = Triple(
            bool, negativeButtonText, negativeButtonClickListener
        )
    }

    fun setNegativeButtonText(text: String) {
        _negativeButtonSetting.value = Triple(
            isNegativeButtonVisible, text, negativeButtonClickListener
        )
    }

    fun setNegativeButtonClickListener(event: () -> Unit) {
        _negativeButtonSetting.value = Triple(
            isNegativeButtonVisible, negativeButtonText, event
        )
    }

    fun setNegativeButton(bool: Boolean, text: String, event: () -> Unit) {
        _negativeButtonSetting.value = Triple(
            bool, text, event
        )
    }

    private val _positiveButtonSetting = MutableStateFlow(Triple(
        false, ""
    ) {})
    val positiveButtonSetting = _positiveButtonSetting.asStateFlow()

    val isPositiveButtonVisible: Boolean
        get() = positiveButtonSetting.value.first

    val positiveButtonText: String
        get() = positiveButtonSetting.value.second

    val positiveButtonClickListener: () -> Unit
        get() = positiveButtonSetting.value.third

    fun setPositiveButtonVisible(bool: Boolean) {
        _positiveButtonSetting.value = Triple(
            bool, positiveButtonText, positiveButtonClickListener
        )
    }

    fun setPositiveButtonText(text: String) {
        _positiveButtonSetting.value = Triple(
            isPositiveButtonVisible, text, positiveButtonClickListener
        )
    }

    fun setPositiveButtonClickListener(event: () -> Unit) {
        _positiveButtonSetting.value = Triple(
            isPositiveButtonVisible, positiveButtonText, event
        )
    }

    fun setPositiveButton(bool: Boolean, text: String, event: () -> Unit) {
        _positiveButtonSetting.value = Triple(
            bool, text, event
        )
    }
}
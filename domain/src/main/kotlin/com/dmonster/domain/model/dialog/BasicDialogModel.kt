package com.dmonster.domain.model.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.view.View
import com.dmonster.domain.model.base.BaseModel

data class BasicDialogModel(
    val titleText: String? = null,
    val text: String? = null,
    val isCheckBoxVisible: Boolean = false,
    val checkBoxText: String? = null,
    val isNegativeButtonVisible: Boolean = false,
    val negativeButtonText: String? = null,
    val isPositiveButtonVisible: Boolean = false,
    val positiveButtonText: String? = null,
): BaseModel {
    override val key: Long
        get() = 0
}

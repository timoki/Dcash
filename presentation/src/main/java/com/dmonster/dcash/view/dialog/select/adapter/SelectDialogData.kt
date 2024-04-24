package com.dmonster.dcash.view.dialog.select.adapter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectDialogData(
    val code: String,
    val item: String,
    val isSelected: Boolean = false,
) : Parcelable
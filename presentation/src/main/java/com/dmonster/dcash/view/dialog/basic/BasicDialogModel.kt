package com.dmonster.dcash.view.dialog.basic

import com.dmonster.domain.model.base.BaseModel

data class BasicDialogModel(
    val titleText: String = "",
    val text: String = "",
    val setCancelable: Boolean = true,
    val setCheckBox: Pair<Boolean, String> = false to "",
    val setNegativeButton: Pair<Boolean, String> = false to "",
    val setPositiveButton: Pair<Boolean, String> = false to "",
    val buttonClickListener: BasicDialog.ButtonClickListener? = null,
): BaseModel {
    override val key: Long
        get() = 0
}

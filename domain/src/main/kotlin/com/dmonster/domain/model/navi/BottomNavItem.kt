package com.dmonster.domain.model.navi

import com.dmonster.domain.model.base.BaseModel

data class BottomNavItem(
    val icon: Long,
    val title: String,
    var isChecked: Boolean,
): BaseModel {
    override val key: Long
        get() = icon
}
package com.dmonster.domain.model.navi

import com.dmonster.domain.model.base.BaseModel

data class BottomNavItem(
    val icon: Int,
    val title: String,
    var isChecked: Boolean,
): BaseModel {
    override val key: Int
        get() = icon
}
package kr.timoky.domain.model.navi

import kr.timoky.domain.model.base.BaseModel

data class NavItem(
    val icon: Int,
    val title: String,
    var isChecked: Boolean,
): BaseModel {
    override val key: Int
        get() = icon
}
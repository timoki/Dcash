package com.dmonster.domain.model.home

import com.dmonster.domain.model.base.BaseModel

data class FilterModel(
    val name: String?,
    val code: String?,
    val order: Int?,
    var isChecked: Boolean = false,
) : BaseModel {
    override val key: String?
        get() = code
}

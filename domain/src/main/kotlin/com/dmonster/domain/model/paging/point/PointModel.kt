package com.dmonster.domain.model.paging.point

import com.dmonster.domain.model.base.BaseModel

data class PointModel(
    val point: Int? = null,
    val hisotry: List<PointHistoryModel>? = null,
) : BaseModel {
    override val key: Any
        get() = 0
}

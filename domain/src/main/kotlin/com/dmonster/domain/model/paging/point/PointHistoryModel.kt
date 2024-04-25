package com.dmonster.domain.model.paging.point

import java.io.Serializable

data class PointHistoryModel(
    val mtIdx: Int,
    val ptType: Int? = null,
    val ptName: String? = null,
    val ptPoint: Int? = null,
    val ptWdate: String? = null,
    val guid: Long? = null,
) : Serializable

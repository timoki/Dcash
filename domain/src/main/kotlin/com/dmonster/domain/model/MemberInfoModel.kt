package com.dmonster.domain.model

import com.dmonster.domain.model.base.BaseModel

data class MemberInfoModel(
    val idx: Long,
    val mt_id: String? = null,
    val mt_name: String? = null,
    val mt_wdate: String? = null,
    val mt_type: Int? = null,
) : BaseModel {
    override val key: Long
        get() = idx
}
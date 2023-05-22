package com.dmonster.domain.model

import com.dmonster.domain.model.base.BaseModel

data class NewsModel(
    val guid: Long,
    val title: String? = null,
    val category: String? = null,
    val link: String? = null,
    val enclosure: String? = null,
    val author: String? = null,
    val creator: String? = null,
    val pubDate: String? = null,
): BaseModel {
    override val key: Long
        get() = guid
}

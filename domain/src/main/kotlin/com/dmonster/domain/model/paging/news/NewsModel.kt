package com.dmonster.domain.model.paging.news

import com.dmonster.domain.model.base.BaseModel
import com.dmonster.domain.model.paging.PageModel

data class NewsModel(
    val page: PageModel,
    val rows: List<NewsListModel>,
): BaseModel {
    override val key: Long
        get() = page.cur_page.toLong()
}

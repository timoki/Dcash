package com.dmonster.data.remote.dto.request

import com.dmonster.data.remote.dto.request.base.BaseRequest

data class NewsRequest(
    val row: Int,
    val search_filter: String?,
    val search_value: String?,
    val search_sdate: String?,
    val search_edate: String?,
    val search_order: String?,
) : BaseRequest()
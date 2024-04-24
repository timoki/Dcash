package com.dmonster.data.remote.dto.request

import com.dmonster.data.remote.dto.request.base.BaseRequest
import kotlinx.coroutines.flow.StateFlow

data class NewsRequest(
    val row: Int,
    val search_filter: StateFlow<String?>?,
    val search_value: StateFlow<String?>?,
    val search_sdate: StateFlow<String?>?,
    val search_edate: StateFlow<String?>?,
    val search_order: StateFlow<String?>?,
    val search_category: StateFlow<String?>?,
    val search_author: StateFlow<String?>?,
    val search_creator: StateFlow<String?>?,
) : BaseRequest()
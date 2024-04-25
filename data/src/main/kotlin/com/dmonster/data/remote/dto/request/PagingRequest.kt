package com.dmonster.data.remote.dto.request

import com.dmonster.data.remote.dto.request.base.BaseRequest
import kotlinx.coroutines.flow.StateFlow

data class PagingRequest(
    val row: Int,
    val search_filter: StateFlow<String?>? = null,
    val search_value: StateFlow<String?>? = null,
    val search_sdate: StateFlow<String?>? = null,
    val search_edate: StateFlow<String?>? = null,
    val search_order: StateFlow<String?>? = null,
    val search_category: StateFlow<String?>? = null,
    val search_author: StateFlow<String?>? = null,
    val search_creator: StateFlow<String?>? = null,
) : BaseRequest()
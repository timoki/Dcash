package com.dmonster.domain.model.paging

data class PageModel(
    val search_value: String?,
    val search_edate2: String?,
    val offset: Int?,
    val end_page: Int?,
    val total_count: Int?,
    val search_order: String?,
    val cur_page: Int,
    val search_type: String?,
    val search_edate: String?,
    val search_sdate2: String?,
    val start_page: Int?,
    val fetch: Int?,
    val search_filter: String?,
    val total_page: Int?,
    val row: Int?,
    val search_sdate: String?,
)
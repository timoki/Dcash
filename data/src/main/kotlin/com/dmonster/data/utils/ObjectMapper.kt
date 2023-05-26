package com.dmonster.data.utils

import com.dmonster.data.local.entity.paging.news.NewsListEntity
import com.dmonster.data.remote.dto.response.MemberInfoDto
import com.dmonster.data.remote.dto.response.PageDto
import com.dmonster.data.remote.dto.response.news.NewsDto
import com.dmonster.data.remote.dto.response.TokenDto
import com.dmonster.data.remote.dto.response.news.NewsListDto
import com.dmonster.domain.model.MemberInfoModel
import com.dmonster.domain.model.paging.news.NewsModel
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.model.paging.PageModel
import com.dmonster.domain.model.paging.news.NewsListModel

object ObjectMapper {
    fun TokenDto.toModel(): TokenModel = TokenModel(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )

    fun MemberInfoDto.toModel(): MemberInfoModel = MemberInfoModel(
        idx = this.idx,
        mt_id = this.mt_id,
        mt_name = this.mt_name,
        mt_wdate = this.mt_wdate,
        mt_type = this.mt_type,
    )

    fun List<NewsListDto>.dtoToNewsListEntityList(): List<NewsListEntity> = map {
        it.toEntity()
    }

    private fun NewsListDto.toEntity(): NewsListEntity = NewsListEntity(
        guid = this.guid,
        title = this.title,
        category = this.category,
        link = this.link,
        enclosure = this.enclosure,
        author = this.author,
        creator = this.creator,
        pubDate = this.pubDate,
    )

    fun NewsDto.toModel(): NewsModel = NewsModel(
        page = this.page.toModel(),
        rows = this.rows.dtoToNewsListModelList()
    )

    private fun PageDto.toModel(): PageModel = PageModel(
        search_value = this.search_value,
        search_edate2 = this.search_edate2,
        offset = this.offset,
        end_page = this.end_page,
        total_count = this.total_count,
        search_order = this.search_order,
        cur_page = this.cur_page,
        search_type = this.search_type,
        search_edate = this.search_edate,
        search_sdate2 = this.search_sdate2,
        start_page = this.start_page,
        fetch = this.fetch,
        search_filter = this.search_filter,
        total_page = this.total_page,
        row = this.row,
        search_sdate = this.search_sdate
    )

    private fun List<NewsListDto>.dtoToNewsListModelList(): List<NewsListModel> = map {
        it.toModel()
    }

    private fun NewsListDto.toModel(): NewsListModel = NewsListModel(
        guid = this.guid,
        title = this.title,
        category = this.category,
        link = this.link,
        enclosure = this.enclosure,
        author = this.author,
        creator = this.creator,
        pubDate = this.pubDate,
    )

    fun List<NewsListEntity>.entityToNewsListModelList(): List<NewsListModel> = map {
        it.toModel()
    }

    fun NewsListEntity.toModel(): NewsListModel = NewsListModel(
        guid = this.guid,
        title = this.title,
        category = this.category,
        link = this.link,
        enclosure = this.enclosure,
        author = this.author,
        creator = this.creator,
        pubDate = this.pubDate,
    )
}
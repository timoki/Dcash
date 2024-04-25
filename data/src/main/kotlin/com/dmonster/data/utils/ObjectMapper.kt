package com.dmonster.data.utils

import com.dmonster.data.local.entity.paging.news.NewsListEntity
import com.dmonster.data.local.entity.paging.point.PointHistoryEntity
import com.dmonster.data.remote.dto.response.MemberInfoDto
import com.dmonster.data.remote.dto.response.PageDto
import com.dmonster.data.remote.dto.response.TokenDto
import com.dmonster.data.remote.dto.response.home.AuthorDto
import com.dmonster.data.remote.dto.response.home.CategoryDto
import com.dmonster.data.remote.dto.response.home.CategoryNewsDto
import com.dmonster.data.remote.dto.response.home.CreatorDto
import com.dmonster.data.remote.dto.response.home.HomeDataDto
import com.dmonster.data.remote.dto.response.news.NewsDto
import com.dmonster.data.remote.dto.response.news.NewsListDto
import com.dmonster.data.remote.dto.response.point.PointDto
import com.dmonster.data.remote.dto.response.point.PointHistoryDto
import com.dmonster.domain.model.MemberInfoModel
import com.dmonster.domain.model.TokenModel
import com.dmonster.domain.model.home.CategoryNewsModel
import com.dmonster.domain.model.home.FilterModel
import com.dmonster.domain.model.home.HomeDataModel
import com.dmonster.domain.model.paging.PageModel
import com.dmonster.domain.model.paging.news.NewsListModel
import com.dmonster.domain.model.paging.news.NewsModel
import com.dmonster.domain.model.paging.point.PointHistoryModel
import com.dmonster.domain.model.paging.point.PointModel

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
        point = this.point,
        viewed = this.viewed,
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
        point = this.point,
        viewed = this.viewed,
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
        point = this.point,
        viewed = this.viewed,
    )

    private fun AuthorDto.toModel(): FilterModel = FilterModel(
        name = this.atName,
        code = this.atCode,
        order = this.atOrder,
    )

    private fun List<AuthorDto>.dtoToAuthorModelList(): List<FilterModel> = map {
        it.toModel()
    }

    private fun CategoryDto.toModel(): FilterModel = FilterModel(
        name = this.ctName,
        code = this.ctCode,
        order = this.ctOrder,
    )

    private fun List<CategoryDto>.dtoToCategoryModelList(): List<FilterModel> = map {
        it.toModel()
    }

    private fun CreatorDto.toModel(): FilterModel = FilterModel(
        name = this.ctName,
        code = this.ctCode,
        order = this.ctOrder,
    )

    private fun List<CreatorDto>.dtoToCreatorModelList(): List<FilterModel> = map {
        it.toModel()
    }

    private fun CategoryNewsDto.toModel(): CategoryNewsModel = CategoryNewsModel(
        ctName = this.ctName,
        news = this.news?.dtoToNewsListModelList(),
    )

    private fun List<CategoryNewsDto>.dtoToCategoryNewsModelList(): List<CategoryNewsModel> = map {
        it.toModel()
    }

    fun HomeDataDto.toModel(): HomeDataModel = HomeDataModel(
        creator = this.creator?.dtoToCreatorModelList(),
        author = this.author?.dtoToAuthorModelList(),
        category = this.category?.dtoToCategoryModelList(),
        custom = this.custom?.dtoToNewsListModelList(),
        banner = this.banner?.dtoToNewsListModelList(),
        categoryNews = this.categoryNews?.dtoToCategoryNewsModelList(),
        recommend = this.recommend?.dtoToNewsListModelList(),
    )

    private fun List<PointHistoryDto>.dtoToPointHistoryModelList(): List<PointHistoryModel> = map {
        it.toModel()
    }

    private fun PointHistoryDto.toModel() = PointHistoryModel(
        mtIdx = this.mtIdx,
        ptType = this.ptType,
        ptName = this.ptName,
        ptPoint = this.ptPoint,
        ptWdate = this.ptWdate,
        guid = this.guid,
    )

    fun PointDto.toModel() = PointModel(
        point = this.point,
        hisotry = this.hisotry?.dtoToPointHistoryModelList(),
    )

    private fun PointHistoryDto.toEntity() = PointHistoryEntity(
        mtIdx = this.mtIdx,
        ptType = this.ptType,
        ptName = this.ptName,
        ptPoint = this.ptPoint,
        ptWdate = this.ptWdate,
        guid = this.guid,
    )

    fun List<PointHistoryDto>.dtoToPointHistoryEntityList(): List<PointHistoryEntity> = map {
        it.toEntity()
    }

    fun PointHistoryEntity.toModel() = PointHistoryModel(
        mtIdx = this.mtIdx,
        ptType = this.ptType,
        ptName = this.ptName,
        ptPoint = this.ptPoint,
        ptWdate = this.ptWdate,
        guid = this.guid,
    )
}
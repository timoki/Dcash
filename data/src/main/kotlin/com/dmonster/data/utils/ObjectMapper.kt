package com.dmonster.data.utils

import com.dmonster.data.local.entity.paging.news.NewsEntity
import com.dmonster.data.remote.dto.MemberInfoDto
import com.dmonster.data.remote.dto.NewsDto
import com.dmonster.data.remote.dto.TokenDto
import com.dmonster.data.utils.ObjectMapper.toModel
import com.dmonster.domain.model.MemberInfoModel
import com.dmonster.domain.model.NewsModel
import com.dmonster.domain.model.TokenModel

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

    fun List<NewsDto>.toNewsEntityList(): List<NewsEntity> = map {
        it.toEntity()
    }

    private fun NewsDto.toEntity(): NewsEntity = NewsEntity(
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
        guid = this.guid,
        title = this.title,
        category = this.category,
        link = this.link,
        enclosure = this.enclosure,
        author = this.author,
        creator = this.creator,
        pubDate = this.pubDate,
    )

    fun NewsEntity.toDto(): NewsDto = NewsDto(
        guid = this.guid,
        title = this.title,
        category = this.category,
        link = this.link,
        enclosure = this.enclosure,
        author = this.author,
        creator = this.creator,
        pubDate = this.pubDate,
    )

    fun NewsEntity.toModel(): NewsModel = NewsModel(
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
package com.dmonster.data.local.entity.paging.news

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "news",
    indices = [
        Index("guid", unique = true)
    ]
)
data class NewsListEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "num")
    val num: Long = 0L,
    @ColumnInfo(name = "guid")
    val guid: Long,
    @ColumnInfo(name = "title")
    val title: String? = null,
    @ColumnInfo(name = "category")
    val category: String? = null,
    @ColumnInfo(name = "link")
    val link: String? = null,
    @ColumnInfo(name = "enclosure")
    val enclosure: String? = null,
    @ColumnInfo(name = "author")
    val author: String? = null,
    @ColumnInfo(name = "creator")
    val creator: String? = null,
    @ColumnInfo(name = "pubDate")
    val pubDate: String? = null,
    @ColumnInfo(name = "point")
    val point: Int? = null,
    @ColumnInfo(name = "viewed")
    val viewed: Int? = null,
)
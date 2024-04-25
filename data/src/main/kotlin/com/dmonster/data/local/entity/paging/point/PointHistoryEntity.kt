package com.dmonster.data.local.entity.paging.point

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "point",
)
data class PointHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "num")
    val num: Long = 0L,
    @ColumnInfo(name = "mtIdx")
    val mtIdx: Int,
    @ColumnInfo(name = "ptType")
    val ptType: Int? = null,
    @ColumnInfo(name = "ptName")
    val ptName: String? = null,
    @ColumnInfo(name = "ptPoint")
    val ptPoint: Int? = null,
    @ColumnInfo(name = "ptWdate")
    val ptWdate: String? = null,
    @ColumnInfo(name = "guid")
    val guid: Long? = null,
)

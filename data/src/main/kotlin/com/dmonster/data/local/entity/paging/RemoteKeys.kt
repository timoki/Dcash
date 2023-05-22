package com.dmonster.data.local.entity.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "paging_remote_key"
)
data class RemoteKeys(
    @PrimaryKey
    val idx: Long,
    val type: String,
    val prevKey: Int?,
    val nextKey: Int?
)

package com.dmonster.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "config",
)
data class ConfigEntity(
    @PrimaryKey
    val idx: Int = 0
)
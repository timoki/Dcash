package com.dmonster.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmonster.data.local.converter.RoomTypeConverters
import com.dmonster.data.local.dao.ConfigDao
import com.dmonster.data.local.dao.paging.NewsDao
import com.dmonster.data.local.dao.paging.PointDao
import com.dmonster.data.local.dao.paging.RemoteKeysDao
import com.dmonster.data.local.entity.ConfigEntity
import com.dmonster.data.local.entity.paging.RemoteKeys
import com.dmonster.data.local.entity.paging.news.NewsListEntity
import com.dmonster.data.local.entity.paging.point.PointHistoryEntity

@Database(
    entities = [
        ConfigEntity::class,
        NewsListEntity::class,
        PointHistoryEntity::class,
        RemoteKeys::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class MyDatabase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
    abstract fun newsDao(): NewsDao
    abstract fun pointDao(): PointDao
    abstract fun remoteKeys(): RemoteKeysDao
}
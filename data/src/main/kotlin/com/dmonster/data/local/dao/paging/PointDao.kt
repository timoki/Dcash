package com.dmonster.data.local.dao.paging

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dmonster.data.local.entity.paging.point.PointHistoryEntity

@Dao
interface PointDao {
    @Query(
        "SELECT * FROM point ORDER BY num ASC"
    )
    fun getPointHistoryList(): PagingSource<Int, PointHistoryEntity>

    @Insert
    suspend fun insertAll(list: List<PointHistoryEntity>)

    @Query(
        "DELETE FROM point"
    )
    suspend fun deleteAll()
}
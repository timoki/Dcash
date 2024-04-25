package com.dmonster.data.local.dao.paging

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dmonster.data.local.entity.paging.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Query(
        "SELECT * FROM paging_remote_key WHERE type = :type AND idx = :idx"
    )
    suspend fun getRemoteKeys(type: String, idx: Long?): RemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeys(keys: List<RemoteKeys>?)

    @Query(
        "DELETE FROM paging_remote_key WHERE type = :type"
    )
    suspend fun removeRemoteKeys(type: String)
}
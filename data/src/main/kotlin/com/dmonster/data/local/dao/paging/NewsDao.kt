package com.dmonster.data.local.dao.paging

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dmonster.data.local.entity.paging.news.NewsEntity
import retrofit2.http.DELETE

@Dao
interface NewsDao {
    @Query(
        "SELECT * FROM news ORDER BY pubDate"
    )
    fun getNewsList(): PagingSource<Int, NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<NewsEntity>)

    @Query(
        "DELETE FROM news"
    )
    suspend fun deleteAll()

    @Query(
        "DELETE FROM news WHERE guid = :id"
    )
    suspend fun deleteNews(id: Long)
}
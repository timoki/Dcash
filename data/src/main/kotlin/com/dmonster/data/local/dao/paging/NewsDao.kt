package com.dmonster.data.local.dao.paging

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dmonster.data.local.entity.paging.news.NewsListEntity

@Dao
interface NewsDao {
    @Query(
        "SELECT * FROM news ORDER BY num ASC"
    )
    fun getNewsList(): PagingSource<Int, NewsListEntity>

    @Insert
    suspend fun insertAll(list: List<NewsListEntity>)

    @Query(
        "DELETE FROM news"
    )
    suspend fun deleteAll()

    @Query(
        "DELETE FROM news WHERE guid = :id"
    )
    suspend fun deleteNews(id: Long)
}
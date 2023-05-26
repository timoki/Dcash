package com.dmonster.data.local.converter

import androidx.room.TypeConverter
import com.dmonster.data.remote.dto.response.news.NewsDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RoomTypeConverters {
    @TypeConverter
    fun stringToMap(value: String): Map<String, String> {
        val type = object: TypeToken<Map<String?, String?>?>(){}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun mapToString(value: Map<String, String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun newsListToString(value: List<NewsDto>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun stringToNewsList(value: String): List<NewsDto> {
        val type = object : TypeToken<List<NewsDto?>?>() {}.type
        return Gson().fromJson(value, type)
    }
}
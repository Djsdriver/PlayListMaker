package com.example.playlistmaker.media.addPlayList.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ListConverter {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(listString: String): List<Int> {
        val gson = Gson()
        return gson.fromJson(listString, object : TypeToken<List<Int>>(){}.type)
    }
}
package com.example.playlistmaker.media.addPlayList.data.db

import androidx.room.TypeConverter
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ListConverter {
    @TypeConverter
    fun listToJson(value: List<TrackEntity>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<TrackEntity> {
        val listType = object : TypeToken<List<TrackEntity>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
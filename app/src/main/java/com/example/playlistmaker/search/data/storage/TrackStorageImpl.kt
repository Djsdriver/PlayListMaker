package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences

import androidx.core.content.edit
import com.example.playlistmaker.media.data.db.AppDatabase

import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking

class TrackStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val appDatabase: AppDatabase
) : TrackStorage {

    companion object {
        private const val KEY_HISTORY_LIST = "history_list"
        private const val MAX_HISTORY_LIST_SIZE = 10
    }

    override fun saveTrackHistoryList(historyList: ArrayList<Track>) {
        val editor = sharedPreferences.edit()
        val jsonString = Gson().toJson(historyList)
        editor.putString(KEY_HISTORY_LIST, jsonString)
        editor.apply()
    }

    override fun clearHistoryList() {
        sharedPreferences.edit {
            clear()
        }
    }

    override fun loadData(): ArrayList<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY_LIST, "[]")
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    override fun saveData(track: Track) {
        // Найти все идентификаторы избранных треков
        val favoriteTrackIds = runBlocking {
            appDatabase.getMovieDao().getAllTrackIds()
        }
        // Пометить трек, если он присутствует в списке избранных треков
        val isFavorite = favoriteTrackIds.contains(track.trackId)
        track.isFavorite = isFavorite

        /*// Добавить трек в историю
        val historyList = loadData()
        historyList.add(track)
        if (historyList.size > MAX_HISTORY_LIST_SIZE) {
            historyList.removeAt(0)
        }
        saveTrackHistoryList(historyList)*/
    }
}

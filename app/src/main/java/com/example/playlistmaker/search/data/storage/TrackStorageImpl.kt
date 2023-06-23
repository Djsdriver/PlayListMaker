package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences

import androidx.core.content.edit

import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackStorageImpl(private val sharedPreferences: SharedPreferences) : TrackStorage {


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
        // addTrack(track)
    }
}

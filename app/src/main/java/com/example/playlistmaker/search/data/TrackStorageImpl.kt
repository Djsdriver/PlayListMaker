package com.example.playlistmaker.search.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit

import com.example.playlistmaker.Const
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackStorageImpl(context: Context) : TrackStorage {

    private val sharedPreferences =
        context.getSharedPreferences(Const.SHARED_PREFERENCES_HISTORY_LIST, MODE_PRIVATE)

    var historyList=ArrayList<Track>()

    override fun addTrack(track: Track) {
        val index = historyList.indexOfFirst { it.trackId == track.trackId }

        if (historyList.size < 10) {
            if (index == -1) {
                historyList.add(0, track)
            } else {
                shiftElementToTopOfHistoryList(index)
            }
        } else {
            if (index == -1) {
                historyList.removeAt(historyList.lastIndex)
                historyList.add(0, track)
            } else {
                shiftElementToTopOfHistoryList(index)
            }
        }
    }

     override fun clearHistoryList() {
        historyList.clear()
        sharedPreferences.edit{
            clear()
        }
    }

    private fun shiftElementToTopOfHistoryList(index: Int) {
        val trackToMove = historyList[index]
        historyList.removeAt(index)
        historyList.add(0, trackToMove)
    }

    override fun loadData(): ArrayList<Track> {
        val json = sharedPreferences.getString(Const.KEY_HISTORY_LIST, "[]")
        val type = object : TypeToken<ArrayList<Track>>() {}.type // заменяем на TypeToken<ArrayList<Track>>()
        historyList = Gson().fromJson(json, type)
        return historyList
    }

    override fun saveData(track: Track) {
        val json = sharedPreferences.getString(Const.KEY_HISTORY_LIST, "[]")
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        historyList = Gson().fromJson(json, type)

        addTrack(track)

        val editor = sharedPreferences.edit()
        val jsonString = Gson().toJson(historyList)
        editor.putString(Const.KEY_HISTORY_LIST, jsonString)
        editor.apply()
    }
}

object JsonConverter {
    inline fun <reified T> itemToJson(item: T): String = Gson().toJson(item)

    inline fun <reified T> itemListToJson(items: ArrayList<T>): String = Gson().toJson(items)

    inline fun <reified T> jsonToItem(json: String): T = Gson().fromJson(json, T::class.java)

    inline fun <reified T> jsonToItemList(json: String): ArrayList<T> =
        Gson().fromJson(json, object : TypeToken<ArrayList<T>>() {}.type)
}
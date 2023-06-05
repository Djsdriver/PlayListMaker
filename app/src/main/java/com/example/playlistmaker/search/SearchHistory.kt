package com.example.playlistmaker.search

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.playlistmaker.Const
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val sharedPreferences: SharedPreferences) {

    var historyList=ArrayList<Track>()

    fun addTrack(track: Track) {
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

    fun clearHistoryList() {
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

    fun loadData(): ArrayList<Track> {
        val json =sharedPreferences.getString(Const.KEY_HISTORY_LIST,"[]")
        val type = object : TypeToken<List<Track>>() {}.type
        historyList= Gson().fromJson(json, type)
        return historyList

    }

     fun saveData() {
        val json = Gson().toJson(historyList)
         sharedPreferences.edit {
             putString(Const.KEY_HISTORY_LIST, json)
         }
    }
}


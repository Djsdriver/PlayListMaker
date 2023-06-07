package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.playlistmaker.search.data.TrackStorageImpl
import com.example.playlistmaker.search.domain.models.Track

class SearchScreenViewModel(application: Application): AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    private val context= application.applicationContext

    private val trackStorageImpl=TrackStorageImpl(context)

    fun addTrack(track: Track){
        trackStorageImpl.addTrack(track)
    }
    fun clearHistoryList(){
        trackStorageImpl.clearHistoryList()
    }
    fun loadData(): ArrayList<Track> {
        return trackStorageImpl.loadData()
    }
    fun saveData(track: Track) {
        trackStorageImpl.saveData(track)
    }
    override fun onCleared() {
        super.onCleared()
    }
}
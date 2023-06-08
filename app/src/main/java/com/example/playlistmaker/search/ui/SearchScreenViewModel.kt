package com.example.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.data.TrackStorageImpl
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.usecase.AddTrackToHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.ClearHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.LoadDataUseCase
import com.example.playlistmaker.search.domain.usecase.SaveDataUseCase

class SearchScreenViewModel(
    private val addTrackToHistoryListUseCase: AddTrackToHistoryListUseCase,
    private val clearHistoryListUseCase: ClearHistoryListUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val saveDataUseCase: SaveDataUseCase
) : ViewModel() {

    fun addTrack(track: Track) {
        addTrackToHistoryListUseCase.execute(track)
    }

    fun clearHistoryList() {
        clearHistoryListUseCase.execute()
    }

    fun loadData(): ArrayList<Track> {
        return loadDataUseCase.execute()
    }

    fun saveData(track: Track) {
        saveDataUseCase.execute(track)
    }

}
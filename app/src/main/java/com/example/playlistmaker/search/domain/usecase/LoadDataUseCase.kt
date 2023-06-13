package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.domain.models.Track

class LoadDataUseCase(private val trackStorage: TrackStorage) {
    fun execute(): ArrayList<Track> {
        return trackStorage.loadData()
    }
}
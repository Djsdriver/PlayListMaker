package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.domain.models.Track

class SaveDataUseCase(private val trackStorage: TrackStorage) {
    fun execute(track: Track) {
        trackStorage.saveData(track)
    }
}
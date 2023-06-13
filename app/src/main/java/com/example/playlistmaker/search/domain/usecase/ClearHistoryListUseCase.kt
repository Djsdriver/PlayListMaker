package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.data.TrackStorage

class ClearHistoryListUseCase(private val trackStorage: TrackStorage) {
    fun execute() {
        trackStorage.clearHistoryList()
    }
}
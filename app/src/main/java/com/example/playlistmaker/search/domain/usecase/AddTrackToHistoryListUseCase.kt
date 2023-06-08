package com.example.playlistmaker.search.domain.usecase

import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.domain.models.Track

class AddTrackToHistoryListUseCase(private val trackStorage: TrackStorage) {

    fun execute(track: Track) {
        val historyList = trackStorage.loadData()

        val index = historyList.indexOfFirst { it.trackId == track.trackId }
        val MAX_HISTORY_LIST_SIZE=10
        if (index != -1) {
            historyList.removeAt(index)
        } else if (historyList.size >= MAX_HISTORY_LIST_SIZE) {
            historyList.removeAt(historyList.lastIndex)
        }
        historyList.add(0, track)

        trackStorage.saveTrackHistoryList(historyList)
    }
}
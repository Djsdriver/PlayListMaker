package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.models.Track

interface TrackStorage {


    fun loadData(): ArrayList<Track>

    fun saveData(track: Track)

    fun clearHistoryList()

    fun addTrack(track: Track)
}
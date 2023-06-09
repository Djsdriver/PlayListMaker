package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.utility.Resource
import com.example.playlistmaker.search.domain.models.Track

interface TrackRepository {

    suspend fun searchTracks(query: String): Resource<List<Track>>
}
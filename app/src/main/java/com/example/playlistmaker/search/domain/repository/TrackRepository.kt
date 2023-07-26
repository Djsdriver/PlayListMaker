package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utility.Resource
import kotlinx.coroutines.flow.Flow


interface TrackRepository {

    suspend fun searchTracks(query: String): Flow<Resource<List<Track>>>
}
package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.utility.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchTrackRepository(
    private val trackApi: TrackApi,
    private val appDatabase: AppDatabase) : TrackRepository {

    override suspend fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
        try {
            val response = trackApi.getTrackByTerm(query)
            if (response.results.isNotEmpty()){
                val tracks = response.results
                /*val favoriteTrackIds = appDatabase.getTrackDao().getAllTrackIds()
                tracks.forEach { track ->
                    track.isFavorite = track.trackId in favoriteTrackIds
                }*/
                emit(Resource.success(tracks))
            } else{
                emit(Resource.error("Проверьте подключение к интернету", null))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.error("Unknown error", null))
        }
    }
}


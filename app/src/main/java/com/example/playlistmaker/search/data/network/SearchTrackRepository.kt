package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.utility.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.Response

class SearchTrackRepository(private val trackApi: TrackApi) : TrackRepository {

    override suspend fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {
        try {
            val response = trackApi.getTrackByTerm(query)
            if (response.results.isNotEmpty()){
                val tracks = response.results
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


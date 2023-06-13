package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.utility.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.TrackRepository
import retrofit2.Call
import retrofit2.Response

class SearchTrackRepository(private val trackApi: TrackApi) : TrackRepository {

    override suspend fun searchTracks(query: String): Resource<List<Track>> {

        val call: Call<TrackResultResponse> = trackApi.getTrackByTerm(query)
        val response: Response<TrackResultResponse> = call.execute()
        return try {
            if (response.isSuccessful) {
                val tracks = response.body()?.results
                Resource.success(tracks)
            } else {
                Resource.error(response.message(), null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.error("Unknown error", null)
        }
    }
}


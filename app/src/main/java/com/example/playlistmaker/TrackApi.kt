package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackApi {

    @GET("/search?entity=song ")
    fun getTrackByTerm(@Query("term") text: String): Call<TrackResultResponse>

}
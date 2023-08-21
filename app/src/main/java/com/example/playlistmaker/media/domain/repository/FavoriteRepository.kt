package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.data.db.TrackEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun insertTrack(trackEntity: TrackEntity)

    suspend fun deleteTrack(trackEntity: TrackEntity)

    fun getAllTracks(): Flow<List<TrackEntity>>

    fun getAllTrackIds(): Flow<List<Int>>

}
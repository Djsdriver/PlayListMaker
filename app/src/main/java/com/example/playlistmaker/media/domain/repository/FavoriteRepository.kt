package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.data.db.TrackEntity
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    suspend fun insertTrack(trackEntity: TrackEntity)

    suspend fun deleteTrack(trackEntity: TrackEntity)

    suspend fun getAllTracks(): Flow<List<TrackEntity>>

}
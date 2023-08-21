package com.example.playlistmaker.media.data.repository

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(private val appDatabase: AppDatabase):FavoriteRepository {
    override suspend fun insertTrack(trackEntity: TrackEntity) {
        appDatabase.getTrackDao().insertTrack(track = trackEntity)
    }

    override suspend fun deleteTrack(trackEntity: TrackEntity) {
        appDatabase.getTrackDao().deleteTrack(track = trackEntity)

    }

    override fun getAllTracks(): Flow<List<TrackEntity>> = appDatabase.getTrackDao().getAllTracks()


    override fun getAllTrackIds(): Flow<List<Int>> = flow{
        val listId= appDatabase.getTrackDao().getAllTrackIds()
        emit(listId)
    }
}
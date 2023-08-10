package com.example.playlistmaker.media.data.repository

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(private val appDatabase: AppDatabase):FavoriteRepository {
    override suspend fun insertTrack(trackEntity: TrackEntity) {
        appDatabase.getMovieDao().insertTrack(track = trackEntity)
    }

    override suspend fun deleteTrack(trackEntity: TrackEntity) {
        appDatabase.getMovieDao().deleteTrack(track = trackEntity)

    }

    override suspend fun getAllTracks(): Flow<List<TrackEntity>> = flow {
        val tracks = appDatabase.getMovieDao().getAllTracks()
        val favoriteTrackIds = tracks.map { it.trackId }
        tracks.forEach { track ->
            track.isFavorite = track.trackId in favoriteTrackIds
        }
        emit(tracks)
    }

    override suspend fun getAllTrackIds(): List<Int> {
        return appDatabase.getMovieDao().getAllTrackIds()
    }
}
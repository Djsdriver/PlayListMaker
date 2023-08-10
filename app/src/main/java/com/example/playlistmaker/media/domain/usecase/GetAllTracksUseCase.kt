package com.example.playlistmaker.media.domain.usecase

import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllTracksUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend fun getAllTracks(): Flow<List<TrackEntity>>{
        return favoriteRepository.getAllTracks()
    }
}

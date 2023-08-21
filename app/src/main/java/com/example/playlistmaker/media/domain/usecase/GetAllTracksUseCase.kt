package com.example.playlistmaker.media.domain.usecase

import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
class GetAllTracksUseCase(private val favoriteRepository: FavoriteRepository) {

    fun getAllTracks(): Flow<List<TrackEntity>>{
        return favoriteRepository.getAllTracks()
    }
}

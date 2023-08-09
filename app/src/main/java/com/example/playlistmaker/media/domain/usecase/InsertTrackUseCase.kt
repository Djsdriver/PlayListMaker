package com.example.playlistmaker.media.domain.usecase

import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteRepository

class InsertTrackUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend fun insertTrack(trackEntity: TrackEntity){
        favoriteRepository.insertTrack(trackEntity = trackEntity)
    }
}
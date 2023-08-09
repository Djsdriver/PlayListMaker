package com.example.playlistmaker.media.domain.usecase

import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteRepository

class DeleteTrackUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend fun deleteTrack(trackEntity: TrackEntity){
        favoriteRepository.deleteTrack(trackEntity = trackEntity)
    }
}
package com.example.playlistmaker.media.domain.usecase

import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteRepository

class AddTrackToFavouriteUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend fun addTrack(trackEntity: TrackEntity){
        favoriteRepository.insertTrack(trackEntity = trackEntity)
    }
}
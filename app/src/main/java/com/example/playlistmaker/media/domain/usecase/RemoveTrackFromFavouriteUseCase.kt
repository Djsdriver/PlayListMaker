package com.example.playlistmaker.media.domain.usecase

import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.repository.FavoriteRepository

class RemoveTrackFromFavouriteUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend fun removeTrack(trackEntity: TrackEntity){
        favoriteRepository.deleteTrack(trackEntity = trackEntity)
    }
}
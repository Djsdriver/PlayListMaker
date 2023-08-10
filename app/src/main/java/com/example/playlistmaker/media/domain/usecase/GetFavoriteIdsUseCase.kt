package com.example.playlistmaker.media.domain.usecase

import com.example.playlistmaker.media.domain.repository.FavoriteRepository

class GetFavoriteIdsUseCase(private val favoriteRepository: FavoriteRepository) {

    suspend fun getFavoriteIds(): List<Int>{
        return favoriteRepository.getAllTrackIds()
    }
}
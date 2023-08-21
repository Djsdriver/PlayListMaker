package com.example.playlistmaker.media.domain.usecase

import com.example.playlistmaker.media.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteIdsUseCase(private val favoriteRepository: FavoriteRepository) {

    fun getFavoriteIds(): Flow<List<Int>> {
        return favoriteRepository.getAllTrackIds()
    }
}
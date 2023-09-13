package com.example.playlistmaker.media.addPlayList.domain.usecase

import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository

class DeleteImageFromStorageUseCase(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(imagePath: String?) {
        playlistRepository.deleteImageFromStorage(imagePath)
    }
}
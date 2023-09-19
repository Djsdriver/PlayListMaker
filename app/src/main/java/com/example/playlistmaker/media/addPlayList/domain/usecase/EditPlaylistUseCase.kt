package com.example.playlistmaker.media.addPlayList.domain.usecase

import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository

class EditPlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun editPlaylist(name: String, description: String, imagePath: String?) {
        playlistRepository.editPlaylist(name,description,imagePath)
    }
}
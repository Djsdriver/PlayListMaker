package com.example.playlistmaker.media.addPlayList.domain.usecase

import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class CreateNewPlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun createNewPlaylist(name: String, description: String, imagePath: String?) {
        playlistRepository.createPlaylist(name,description,imagePath)
    }
}
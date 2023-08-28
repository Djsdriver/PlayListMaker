package com.example.playlistmaker.media.addPlayList.domain.usecase

import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository

class InsertPlayListToDatabaseUseCase(private val playlistRepository: PlaylistRepository) {
    suspend fun invoke(playlist: PlaylistEntity) {
        playlistRepository.insertPlaylist(playlist)
    }
}
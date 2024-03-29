package com.example.playlistmaker.media.addPlayList.domain.usecase

import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository


class UpdatePlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(playlistModel: PlaylistModel) {
        playlistRepository.updatePlaylist(playlistModel)
    }
}
package com.example.playlistmaker.media.addPlayList.domain.usecase

import com.example.playlistmaker.media.addPlayList.domain.models.PlaylistModel
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository


class AddTrackToPlaylistUseCase(private val playlistRepository: PlaylistRepository) {

    suspend operator fun invoke(playlistModel: PlaylistModel) {
        playlistRepository.updatePlaylist(playlistModel)
    }
}
package com.example.playlistmaker.media.addPlayList.domain.usecase

import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class GetAllPlaylistToListUseCase(private val playlistRepository: PlaylistRepository) {
    fun getAllTracks(): Flow<List<PlaylistEntity>> {
        return playlistRepository.getAllPlaylists()
    }
}
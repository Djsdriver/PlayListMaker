package com.example.playlistmaker.media.playlistcontent.domain

import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.playlistcontent.domain.repository.PlaylistContentRepository
import kotlinx.coroutines.flow.Flow

class GetPlaylistByIdUsecase(private val playlistContentRepository: PlaylistContentRepository) {
    operator fun invoke(playlistId: Int): PlaylistEntity {
        return playlistContentRepository.getPlaylistById(playlistId)
    }
}
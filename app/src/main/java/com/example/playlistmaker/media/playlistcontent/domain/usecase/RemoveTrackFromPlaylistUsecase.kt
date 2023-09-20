package com.example.playlistmaker.media.playlistcontent.domain.usecase

import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.playlistcontent.domain.repository.PlaylistContentRepository

class RemoveTrackFromPlaylistUsecase(
    private val playlistContentRepository: PlaylistContentRepository
) {
    suspend operator fun invoke(track: TrackEntity, playlistId: Int) {
       // playlistContentRepository.removeTrackFromPlaylist(track, playlistId)
    }
}
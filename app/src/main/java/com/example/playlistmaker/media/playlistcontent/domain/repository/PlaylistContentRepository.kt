package com.example.playlistmaker.media.playlistcontent.domain.repository

import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistContentRepository {

    suspend fun removeTrackFromPlaylist(track: TrackEntity, playlistId: Int)

    fun getPlaylistById(playlistId: Int): PlaylistEntity

}
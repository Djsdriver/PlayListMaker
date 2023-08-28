package com.example.playlistmaker.media.addPlayList.domain.repository


import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    suspend fun insertPlaylist(playlist: PlaylistEntity)
}
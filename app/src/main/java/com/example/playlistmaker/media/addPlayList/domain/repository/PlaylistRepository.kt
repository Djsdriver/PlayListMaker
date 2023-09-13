package com.example.playlistmaker.media.addPlayList.domain.repository


import android.net.Uri
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.models.PlaylistModel
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    suspend fun insertPlaylist(playlist: PlaylistEntity)

    suspend fun updatePlaylist(playlistModel: PlaylistModel)

    suspend fun saveImageToPrivateStorage(uri: Uri, nameOfImage: String): String

    suspend fun createPlaylist(name: String, description: String, imagePath: String?)

    suspend fun deletePlaylist(playlistModel: PlaylistModel)

    suspend fun deleteImageFromStorage(imagePath: String?)

}
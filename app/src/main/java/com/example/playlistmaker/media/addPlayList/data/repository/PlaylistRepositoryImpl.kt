package com.example.playlistmaker.media.addPlayList.data.repository


import com.example.playlistmaker.media.addPlayList.data.db.AppDatabasePlayList
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.models.PlaylistModel
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.utility.toPlaylistEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class PlaylistRepositoryImpl(private val appDatabase: AppDatabasePlayList): PlaylistRepository {
    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> = appDatabase.getPlaylistDao().getAllPlaylists()


    override suspend fun insertPlaylist(playlist: PlaylistEntity) {
        appDatabase.getPlaylistDao().insertPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlistModel: PlaylistModel) {
        appDatabase.getPlaylistDao().updatePlaylist(playlistModel.toPlaylistEntity())
    }



}
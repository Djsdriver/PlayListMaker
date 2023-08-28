package com.example.playlistmaker.media.addPlayList.data.repository


import com.example.playlistmaker.media.addPlayList.data.db.AppDatabasePlayList
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl(private val appDatabase: AppDatabasePlayList): PlaylistRepository {
    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> = appDatabase.getPlaylistDao().getAllPlaylists()


    override suspend fun insertPlaylist(playlist: PlaylistEntity) {
        appDatabase.getPlaylistDao().insertPlaylist(playlist)
    }
}
package com.example.playlistmaker.media.playlistcontent.data.repository

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.playlistmaker.media.addPlayList.data.db.AppDatabasePlayList
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.media.playlistcontent.domain.repository.PlaylistContentRepository
import kotlinx.coroutines.flow.Flow

class PlaylistContentRepositoryImpl(private val appDatabase: AppDatabasePlayList): PlaylistContentRepository {
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun removeTrackFromPlaylist(track: TrackEntity, playlistId: Int) {
        val playlist = appDatabase.getPlaylistDao().getPlaylistById(playlistId)
        playlist.tracks.removeIf { it.trackId == track.trackId }
        appDatabase.getPlaylistDao().updatePlaylist(playlist)
    }

    override fun getPlaylistById(playlistId: Int): PlaylistEntity {
        return appDatabase.getPlaylistDao().getPlaylistById(playlistId)
    }


}
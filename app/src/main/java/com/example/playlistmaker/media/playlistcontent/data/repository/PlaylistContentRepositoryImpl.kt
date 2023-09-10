package com.example.playlistmaker.media.playlistcontent.data.repository

import android.net.Uri
import com.example.playlistmaker.media.addPlayList.data.db.AppDatabasePlayList
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.media.playlistcontent.domain.repository.PlaylistContentRepository
import kotlinx.coroutines.flow.Flow

class PlaylistContentRepositoryImpl(private val appDatabase: AppDatabasePlayList): PlaylistContentRepository {
    override suspend fun removeTrackFromPlaylist(track: TrackEntity, playlistId: Int) {
        //appDatabase.getPlaylistDao().removeTrackFromPlaylist(track, playlistId)
    }

    override fun getPlaylistById(playlistId: Int): PlaylistEntity {
        return appDatabase.getPlaylistDao().getPlaylistById(playlistId)
    }


}
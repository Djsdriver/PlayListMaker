package com.example.playlistmaker.playlist.data.db

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.PlaylistEntity
import kotlinx.coroutines.flow.Flow

interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE playlistId =:id")
    suspend fun getPlaylist(id: Long): PlaylistEntity

    @Query("SELECT * FROM playlist_table")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
}
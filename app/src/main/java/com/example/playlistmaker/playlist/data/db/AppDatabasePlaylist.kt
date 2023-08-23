package com.example.playlistmaker.playlist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.ItemPlaylist

@Database(
    version = 1,
    entities = [
        ItemPlaylist::class
    ]
)
abstract class AppDatabasePlaylist : RoomDatabase() {

    abstract fun getPlaylist(): PlaylistDao
}
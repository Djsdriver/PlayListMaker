package com.example.playlistmaker.media.addPlayList.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database([PlaylistEntity::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class AppDatabasePlayList: RoomDatabase() {

    abstract fun getPlaylistDao(): PlaylistDao
}
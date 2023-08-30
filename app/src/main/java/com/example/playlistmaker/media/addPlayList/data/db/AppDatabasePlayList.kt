package com.example.playlistmaker.media.addPlayList.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.playlistmaker.media.data.db.TrackEntity


@Database([PlaylistEntity::class, TrackEntity::class], version = 1)
@TypeConverters(ListConverter::class)
abstract class AppDatabasePlayList: RoomDatabase() {

    abstract fun getPlaylistDao(): PlaylistDao
}
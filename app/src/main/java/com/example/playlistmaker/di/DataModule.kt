package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.media.addPlayList.data.db.AppDatabasePlayList
import com.example.playlistmaker.media.data.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "itunes_database.db")
            .build()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabasePlayList::class.java, "playlist_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}
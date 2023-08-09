package com.example.playlistmaker.di

import com.example.playlistmaker.media.data.repository.FavoriteRepositoryImpl
import com.example.playlistmaker.media.domain.repository.FavoriteRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get())
    }

}
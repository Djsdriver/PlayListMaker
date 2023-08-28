package com.example.playlistmaker.di

import com.example.playlistmaker.media.domain.usecase.RemoveTrackFromFavouriteUseCase
import com.example.playlistmaker.media.domain.usecase.GetAllTracksUseCase
import com.example.playlistmaker.media.domain.usecase.GetFavoriteIdsUseCase
import com.example.playlistmaker.media.domain.usecase.AddTrackToFavouriteUseCase
import com.example.playlistmaker.media.ui.FavoriteTracksViewModel
import com.example.playlistmaker.media.ui.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {

    viewModel {
        FavoriteTracksViewModel(get())
    }


    factory { RemoveTrackFromFavouriteUseCase( favoriteRepository = get()) }

    factory { GetAllTracksUseCase(favoriteRepository = get()) }

    factory { AddTrackToFavouriteUseCase(favoriteRepository = get())}

    factory { GetFavoriteIdsUseCase(favoriteRepository = get())}


}
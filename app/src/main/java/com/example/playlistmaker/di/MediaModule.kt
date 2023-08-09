package com.example.playlistmaker.di

import com.example.playlistmaker.media.domain.usecase.DeleteTrackUseCase
import com.example.playlistmaker.media.domain.usecase.GetAllTracksUseCase
import com.example.playlistmaker.media.domain.usecase.InsertTrackUseCase
import com.example.playlistmaker.media.ui.FavoriteTracksViewModel
import com.example.playlistmaker.media.ui.PlayListsViewModel
import com.example.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.player.ui.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.usecase.AddTrackToHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.ClearHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.LoadDataUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {

    viewModel {
        FavoriteTracksViewModel(get())
    }

    viewModel{
        PlayListsViewModel()
    }

    factory { DeleteTrackUseCase( favoriteRepository = get()) }

    factory { GetAllTracksUseCase(favoriteRepository = get()) }

    factory { InsertTrackUseCase(favoriteRepository = get())}


}
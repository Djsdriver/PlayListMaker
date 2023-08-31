package com.example.playlistmaker.di

import com.example.playlistmaker.media.addPlayList.data.repository.PlaylistRepositoryImpl
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import com.example.playlistmaker.media.addPlayList.domain.usecase.GetAllPlaylistToListUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import com.example.playlistmaker.media.ui.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val playlist = module {


    viewModel {
        PlaylistViewModel(get())
    }

    single<PlaylistRepository> { PlaylistRepositoryImpl(get()) }


    factory { InsertPlayListToDatabaseUseCase (get()) }
    factory { GetAllPlaylistToListUseCase (get()) }




}
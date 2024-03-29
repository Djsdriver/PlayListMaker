package com.example.playlistmaker.di

import com.example.playlistmaker.media.addPlayList.domain.usecase.CreateNewPlaylistUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.media.addPlayList.presention.ui.NewPlaylistFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val addPlaylistModule = module {

    viewModel {
        NewPlaylistFragmentViewModel(get(),get(),get())
    }


    factory { InsertPlayListToDatabaseUseCase(playlistRepository = get()) }
    factory { SaveImageToPrivateStorageUseCase(playlistRepository = get()) }
    factory { CreateNewPlaylistUseCase(playlistRepository = get()) }
}
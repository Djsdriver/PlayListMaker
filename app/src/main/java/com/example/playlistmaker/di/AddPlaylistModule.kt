package com.example.playlistmaker.di

import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.media.addPlayList.presention.ui.AddFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val addPlaylistModule = module {

    viewModel {
        AddFragmentViewModel(get(),get())
    }


    factory { InsertPlayListToDatabaseUseCase(playlistRepository = get()) }
    factory { SaveImageToPrivateStorageUseCase(playlistRepository = get()) }
}
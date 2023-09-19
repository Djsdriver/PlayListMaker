package com.example.playlistmaker.di

import com.example.playlistmaker.media.addPlayList.domain.usecase.CreateNewPlaylistUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.DeleteImageFromStorageUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.EditPlaylistUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.EditUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.UpdatePlaylistUseCase
import com.example.playlistmaker.media.editPlaylistFragment.presentation.EditPlaylistFragmentViewModel
import com.example.playlistmaker.media.playlistcontent.domain.GetPlaylistByIdUsecase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val editPlaylistModule = module {

    viewModel {
        EditPlaylistFragmentViewModel(get(),get(),get(),get(),get(),get(),get(),get())
    }


    factory { GetPlaylistByIdUsecase(playlistContentRepository = get()) }
    factory { SaveImageToPrivateStorageUseCase(playlistRepository = get()) }
    factory { CreateNewPlaylistUseCase(playlistRepository = get()) }
    factory { InsertPlayListToDatabaseUseCase(playlistRepository = get()) }
    factory { DeleteImageFromStorageUseCase(playlistRepository = get()) }
    factory { EditPlaylistUseCase(playlistRepository = get()) }
    factory { UpdatePlaylistUseCase(playlistRepository = get()) }
    factory { EditUseCase(playlistRepository = get()) }
}
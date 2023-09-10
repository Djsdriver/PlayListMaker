package com.example.playlistmaker.di

import com.example.playlistmaker.media.addPlayList.data.repository.PlaylistRepositoryImpl
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import com.example.playlistmaker.media.addPlayList.domain.usecase.DeletePlaylistUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import com.example.playlistmaker.media.data.repository.FavoriteRepositoryImpl
import com.example.playlistmaker.media.domain.repository.FavoriteRepository
import com.example.playlistmaker.media.playlistcontent.data.repository.PlaylistContentRepositoryImpl
import com.example.playlistmaker.media.playlistcontent.domain.GetPlaylistByIdUsecase
import com.example.playlistmaker.media.playlistcontent.domain.repository.PlaylistContentRepository
import com.example.playlistmaker.media.playlistcontent.domain.usecase.RemoveTrackFromPlaylistUsecase
import com.example.playlistmaker.media.playlistcontent.presentation.ui.PlaylistContentFragmentViewModel
import com.example.playlistmaker.media.ui.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {

    viewModel {
        PlaylistContentFragmentViewModel(get(),get(),get(),get())
    }

    single<PlaylistContentRepository> { PlaylistContentRepositoryImpl(get()) }


    factory { GetPlaylistByIdUsecase (get()) }
    factory { RemoveTrackFromPlaylistUsecase (get()) }
    factory { DeletePlaylistUseCase (get()) }

}
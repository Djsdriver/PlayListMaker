package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.data.network.SearchTrackRepository
import com.example.playlistmaker.search.data.storage.TrackStorageImpl
import com.example.playlistmaker.search.domain.repository.TrackRepository
import com.example.playlistmaker.search.domain.usecase.AddTrackToHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.ClearHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.LoadDataUseCase
import com.example.playlistmaker.search.domain.usecase.SaveDataUseCase
import com.example.playlistmaker.search.ui.SearchScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule= module {

    single<TrackStorage> { TrackStorageImpl(sharedPreferences = get()) }

    single<TrackRepository> { SearchTrackRepository(trackApi = get()) }

    factory { AddTrackToHistoryListUseCase(trackStorage = get()) }

    factory { ClearHistoryListUseCase(trackStorage = get()) }

    factory { LoadDataUseCase(trackStorage = get()) }

    factory { SaveDataUseCase(trackStorage = get()) }

    viewModel {
        SearchScreenViewModel(
            addTrackToHistoryListUseCase = get(),
            clearHistoryListUseCase = get(),
            loadDataUseCase = get(),
            saveDataUseCase = get(),
            repository = get(),
        )
    }
}

    /*val searchModule = module {
        viewModel { SearchScreenViewModel(get(), get(), get(), get(), get()) }
        single { SearchTrackRepository(get()) }
        single { TrackStorageImpl(get()) }

    }*/


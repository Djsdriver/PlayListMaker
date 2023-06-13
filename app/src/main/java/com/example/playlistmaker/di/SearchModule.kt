package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.search.data.TrackStorage
import com.example.playlistmaker.search.data.network.SearchTrackRepository
import com.example.playlistmaker.search.data.network.TrackApi
import com.example.playlistmaker.search.data.storage.TrackStorageImpl
import com.example.playlistmaker.search.domain.repository.TrackRepository
import com.example.playlistmaker.search.domain.usecase.AddTrackToHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.ClearHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.LoadDataUseCase
import com.example.playlistmaker.search.domain.usecase.SaveDataUseCase
import com.example.playlistmaker.search.ui.SearchScreenViewModel
import com.example.playlistmaker.utility.Const
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule= module {

    single(named(Const.SHARED_PREFERENCES_HISTORY_LIST)) {
        androidContext()
            .getSharedPreferences(Const.SHARED_PREFERENCES_HISTORY_LIST, Context.MODE_PRIVATE)
    }

    single<TrackStorage> { TrackStorageImpl(sharedPreferences = get(qualifier = named(Const.SHARED_PREFERENCES_HISTORY_LIST) )) }
    single<TrackApi>(named("api")){
        Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackApi::class.java)
    }

    single<TrackRepository> { SearchTrackRepository(trackApi = get(qualifier = named("api")) )}

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




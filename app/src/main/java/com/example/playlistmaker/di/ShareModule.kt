package com.example.playlistmaker.di

import com.example.playlistmaker.sharing.data.repository.ExternalNavigator
import com.example.playlistmaker.sharing.data.repository.SharingRepositoryImpl
import com.example.playlistmaker.sharing.domain.repository.SharingRepository
import com.example.playlistmaker.sharing.domain.usecase.OpenTerms
import com.example.playlistmaker.sharing.domain.usecase.SendToSupport
import com.example.playlistmaker.sharing.domain.usecase.ShareApp
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val shareModule= module {

    single<SharingRepository> {
        SharingRepositoryImpl(externalNavigator = get())
    }

    single {
        ExternalNavigator(context = get())
    }

    factory { OpenTerms(sharingRepository = get()) }
    factory { SendToSupport(sharingRepository = get()) }
    factory { ShareApp(sharingRepository = get()) }




}
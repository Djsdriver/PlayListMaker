package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.player.ui.AudioPlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {
    single<AudioPlayerRepository> { AudioPlayerRepositoryImpl() }
    factory { GetCurrentStateUseCase(audioPlayerRepository = get()) }
    factory { PausePlayerUseCase(audioPlayerRepository = get()) }
    factory { PlayBackControlUseCase(audioPlayerRepository = get()) }
    factory { PrepareUseCase(audioPlayerRepository = get()) }
    factory { StartPlayerUseCase(audioPlayerRepository = get()) }
    factory { GetCurrentTimeUseCase(audioPlayerRepository = get()) }
    viewModel { AudioPlayerViewModel(
        getCurrentStateUseCase = get(),
        pausePlayerUseCase = get(),
        playBackControlUseCase = get(),
        prepareUseCase = get(),
        startPlayerUseCase = get(),
        getCurrentTimeUseCase = get()) }
}
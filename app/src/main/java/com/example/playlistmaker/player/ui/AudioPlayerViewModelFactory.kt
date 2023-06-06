package com.example.playlistmaker.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.search.domain.models.Track

class AudioPlayerViewModelFactory(): ViewModelProvider.Factory {



    private val audioPlayerRepositoryImpl= AudioPlayerRepositoryImpl()
    private val getCurrentStateUseCase by lazy { GetCurrentStateUseCase(audioPlayerRepositoryImpl) }
    private val pausePlayerUseCase by lazy { PausePlayerUseCase(audioPlayerRepositoryImpl) }
    private val playBackControlUseCase by lazy { PlayBackControlUseCase(audioPlayerRepositoryImpl) }
    private val prepareUseCase by lazy { PrepareUseCase(audioPlayerRepositoryImpl,) }
    private val startPlayerUseCase by lazy { StartPlayerUseCase(audioPlayerRepositoryImpl) }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AudioPlayerViewModel(
            /*getCurrentStateUseCase = getCurrentStateUseCase,
        pausePlayerUseCase = pausePlayerUseCase,
            playBackControlUseCase = playBackControlUseCase,
            prepareUseCase = prepareUseCase,
            startPlayerUseCase = startPlayerUseCase*/
        audioPlayerRepositoryImpl) as T
    }
}
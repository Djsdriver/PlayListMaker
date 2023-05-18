package com.example.playlistmaker.domain

import com.example.playlistmaker.data.PlayerState
import com.example.playlistmaker.presentation.repository.AudioPlayerRepository

class GetCurrentStateUseCase(private val audioPlayerRepository: AudioPlayerRepository) {

    fun getCurrentState(): PlayerState {
        return audioPlayerRepository.getCurrentState()
    }
}
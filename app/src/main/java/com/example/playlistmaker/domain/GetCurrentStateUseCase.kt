package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.repository.AudioPlayerRepository

class GetCurrentStateUseCase(private val audioPlayerRepository: AudioPlayerRepository) {

    fun getCurrentState(): PlayerState {
        return audioPlayerRepository.getCurrentState()
    }
}
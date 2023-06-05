package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.models.PlayerState

class GetCurrentStateUseCase(private val audioPlayerRepository: AudioPlayerRepository) {

    fun getCurrentState(): PlayerState {
        return audioPlayerRepository.getCurrentState()
    }
}
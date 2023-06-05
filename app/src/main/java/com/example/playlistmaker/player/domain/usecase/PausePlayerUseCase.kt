package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository

class PausePlayerUseCase(private val audioPlayerRepository: AudioPlayerRepository) {

    fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }
}
package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.repository.AudioPlayerRepository

class PausePlayerUseCase(private val audioPlayerRepository: AudioPlayerRepository) {

    fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }
}
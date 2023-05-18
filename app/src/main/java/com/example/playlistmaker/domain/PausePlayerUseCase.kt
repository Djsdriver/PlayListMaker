package com.example.playlistmaker.domain

import com.example.playlistmaker.presentation.repository.AudioPlayerRepository

class PausePlayerUseCase(private val audioPlayerRepository: AudioPlayerRepository) {

    fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }
}
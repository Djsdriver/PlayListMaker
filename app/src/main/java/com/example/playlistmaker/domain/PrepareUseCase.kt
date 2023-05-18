package com.example.playlistmaker.domain

import com.example.playlistmaker.Track
import com.example.playlistmaker.presentation.repository.AudioPlayerRepository

class PrepareUseCase(private val audioPlayerRepository: AudioPlayerRepository, val track: Track) {

    fun prepare() {
        audioPlayerRepository.preparePlayer(track)
    }
}
package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.AudioPlayerRepository

class PrepareUseCase(private val audioPlayerRepository: AudioPlayerRepository, val track: Track) {

    fun prepare() {
        audioPlayerRepository.preparePlayer(track)
    }
}
package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository

class PrepareUseCase(private val audioPlayerRepository: AudioPlayerRepository, val track: Track) {

    fun prepare() {
        audioPlayerRepository.preparePlayer(track)
    }
}
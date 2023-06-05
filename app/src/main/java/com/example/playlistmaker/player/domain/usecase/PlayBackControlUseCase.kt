package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository

class PlayBackControlUseCase(private val audioPlayerRepository: AudioPlayerRepository) {


    fun playBackControl(){
        audioPlayerRepository.playbackControl()
    }
}
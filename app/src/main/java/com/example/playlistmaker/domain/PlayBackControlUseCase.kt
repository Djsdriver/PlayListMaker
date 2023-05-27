package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.repository.AudioPlayerRepository

class PlayBackControlUseCase(private val audioPlayerRepository: AudioPlayerRepository) {


    fun playBackControl(){
        audioPlayerRepository.playbackControl()
    }
}
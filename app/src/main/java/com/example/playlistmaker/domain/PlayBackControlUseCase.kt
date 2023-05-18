package com.example.playlistmaker.domain

import com.example.playlistmaker.presentation.repository.AudioPlayerRepository

class PlayBackControlUseCase(private val audioPlayerRepository: AudioPlayerRepository) {


    fun playBackControl(){
        audioPlayerRepository.playbackControl()
    }
}
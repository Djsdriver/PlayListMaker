package com.example.playlistmaker.domain


import com.example.playlistmaker.domain.repository.AudioPlayerRepository

class StartPlayerUseCase(private val audioPlayerRepository: AudioPlayerRepository) {


    fun startPlayer(){
        audioPlayerRepository.startPlayer()
    }


}
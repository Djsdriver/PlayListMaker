package com.example.playlistmaker.player.domain.usecase


import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository

class StartPlayerUseCase(private val audioPlayerRepository: AudioPlayerRepository) {


    fun startPlayer(){
        audioPlayerRepository.startPlayer()
    }


}
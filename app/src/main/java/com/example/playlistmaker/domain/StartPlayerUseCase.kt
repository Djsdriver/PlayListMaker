package com.example.playlistmaker.domain


import com.example.playlistmaker.data.AudioPlayerRepositoryImpl
import com.example.playlistmaker.presentation.repository.AudioPlayerRepository

class StartPlayerUseCase(private val audioPlayerRepository: AudioPlayerRepository) {


    fun startPlayer(){
        audioPlayerRepository.startPlayer()
    }


}
package com.example.playlistmaker.player.domain.usecase

import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository

class GetCurrentTimeUseCase(private val audioPlayerRepository: AudioPlayerRepository) {


    fun getTime(): Long? {
       return audioPlayerRepository.getCurrentTime()
    }
}
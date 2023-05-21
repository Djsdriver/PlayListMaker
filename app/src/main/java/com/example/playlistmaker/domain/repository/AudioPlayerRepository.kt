package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.PlayerState
import com.example.playlistmaker.domain.models.Track

interface AudioPlayerRepository {

    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(track: Track)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun getCurrentTime(): Int

    fun playbackControl()

    fun getCurrentState(): PlayerState

}
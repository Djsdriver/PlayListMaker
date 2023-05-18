package com.example.playlistmaker.presentation.repository

import com.example.playlistmaker.Track
import com.example.playlistmaker.data.PlayerState

interface AudioPlayerRepository {

    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(track: Track)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun getCurrentTime(): Int

    fun playbackControl()

    fun getCurrentState(): PlayerState

}
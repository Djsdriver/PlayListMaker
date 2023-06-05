package com.example.playlistmaker.player.domain.repository

import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track

interface AudioPlayerRepository {

    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(track: Track)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun getCurrentTime(): Int

    fun playbackControl()

    fun getCurrentState(): PlayerState

}
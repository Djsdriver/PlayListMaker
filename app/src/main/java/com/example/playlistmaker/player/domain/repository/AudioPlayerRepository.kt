package com.example.playlistmaker.player.domain.repository

import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.search.domain.models.Track

interface AudioPlayerRepository {

    fun startPlayer()

    fun pausePlayer()

    fun preparePlayer(
        track: Track,
        onPrepared: () -> Unit,
        onComplete: () -> Unit
    )

    fun setOnCompletionListener(onComplete: (PlayerState) -> Unit)

    fun getCurrentTime(): Long?

    fun playbackControl()

    fun getCurrentState(): PlayerState

}
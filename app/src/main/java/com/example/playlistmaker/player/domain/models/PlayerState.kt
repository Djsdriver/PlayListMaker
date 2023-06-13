package com.example.playlistmaker.player.domain.models

import com.example.playlistmaker.search.domain.models.Track

sealed class PlayerState {
    object Idle : PlayerState()
    data class Prepared(val track: Track) : PlayerState()
    object Playing : PlayerState()
    object Paused : PlayerState()
    data class Progress(val currentPosition: Int) : PlayerState()
    object Completed : PlayerState()
    data class Error(val errorMessage: String) : PlayerState()
}
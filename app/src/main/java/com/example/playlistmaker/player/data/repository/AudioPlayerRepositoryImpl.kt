package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.usecase.*


class AudioPlayerRepositoryImpl(
) : AudioPlayerRepository {

    private var mediaPlayer: MediaPlayer? = null
    private var playerState: PlayerState = PlayerState.Idle

    init {
        mediaPlayer = MediaPlayer()
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        playerState = PlayerState.Playing
        Log.d("My", "$playerState")
    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        playerState = PlayerState.Paused
        Log.d("My", "$playerState")
    }

    override fun preparePlayer(track: Track, onPrepared: () -> Unit, onComplete: () -> Unit) {
        mediaPlayer?.apply {
            reset()
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerState.Prepared(track)
                Log.d("My", "$playerState")
                onPrepared()
            }
            setOnCompletionListener {
                playerState = PlayerState.Completed
                onComplete()
            }
        }
    }


    override fun setOnCompletionListener(onComplete: (PlayerState) -> Unit) {
        mediaPlayer?.setOnCompletionListener {
            playerState = PlayerState.Completed
            onComplete.invoke(PlayerState.Completed)
        }
    }

    override fun getCurrentTime(): Long? = mediaPlayer?.currentPosition?.toLong()


    override fun playbackControl() {
        when (playerState) {
            PlayerState.Playing -> {
                pausePlayer()
            }
            is PlayerState.Prepared, PlayerState.Paused, PlayerState.Completed -> {
                startPlayer()
            }
            else -> {}
        }
    }

    override fun getCurrentState(): PlayerState {
        return playerState
    }

}


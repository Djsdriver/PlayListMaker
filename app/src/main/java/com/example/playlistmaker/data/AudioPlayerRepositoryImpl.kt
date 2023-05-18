package com.example.playlistmaker.data

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.Track
import com.example.playlistmaker.presentation.repository.AudioPlayerRepository


enum class PlayerState {
    STATE_DEFAULT,
    STATE_PREPARED,
    STATE_PLAYING,
    STATE_PAUSED
}

class AudioPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer
) : AudioPlayerRepository {

    private var playerState: PlayerState = PlayerState.STATE_DEFAULT


    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
        Log.d("My", "$playerState")

    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED
        Log.d("My", "$playerState")
    }

    override fun preparePlayer(track: Track) {
        mediaPlayer.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerState.STATE_PREPARED
                Log.d("My", "$playerState")
            }

        }
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            Log.d("My", "$playerState")
        }
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }
            else -> {}
        }
    }

    override fun getCurrentState(): PlayerState {
        return playerState
    }

}


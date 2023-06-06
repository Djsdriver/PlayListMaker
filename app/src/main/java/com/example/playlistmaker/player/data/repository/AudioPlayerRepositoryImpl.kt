package com.example.playlistmaker.player.data.repository

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository


class AudioPlayerRepositoryImpl(
) : AudioPlayerRepository {

    private var mediaPlayer: MediaPlayer? = null
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT

    init {
        mediaPlayer=MediaPlayer()
    }

    override fun startPlayer() {
        mediaPlayer?.start()
        playerState = PlayerState.STATE_PLAYING
        Log.d("My", "$playerState")

    }

    override fun pausePlayer() {
        mediaPlayer?.pause()
        playerState = PlayerState.STATE_PAUSED
        Log.d("My", "$playerState")
    }

    override fun preparePlayer(track: Track) {
        mediaPlayer?.apply {
            setDataSource(track.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = PlayerState.STATE_PREPARED
                Log.d("My", "$playerState")
            }

        }
    }


    override fun setOnCompletionListener(onComplete: () -> Unit) {
        mediaPlayer?.setOnCompletionListener {
            playerState = PlayerState.STATE_COMPLETED
            onComplete.invoke()
        }
    }

    override fun getCurrentTime(): Long? = mediaPlayer?.currentPosition?.toLong()


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


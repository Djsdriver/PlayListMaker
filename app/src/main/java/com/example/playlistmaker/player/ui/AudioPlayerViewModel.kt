package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utility.Const
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(private val getCurrentStateUseCase: GetCurrentStateUseCase,
                           private val pausePlayerUseCase : PausePlayerUseCase,
                           private val playBackControlUseCase: PlayBackControlUseCase,
                           private val prepareUseCase: PrepareUseCase,
                           private val startPlayerUseCase: StartPlayerUseCase,
private val getCurrentTimeUseCase: GetCurrentTimeUseCase) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _playbackTime = MutableLiveData<String?>()
    val playbackTime: LiveData<String?> = _playbackTime

    private val _playbackTimeEnd = MutableLiveData<String?>()
    val playbackTimeEnd: LiveData<String?> = _playbackTimeEnd

    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler = Handler(Looper.getMainLooper())

    fun preparePlayer(track: Track) {
        prepareUseCase.prepare(
            track = track,
            onPrepared = { _playerState.postValue(PlayerState.Prepared(track))},
            onComplete = {
                _playerState.postValue(PlayerState.Completed)
                handler.removeCallbacksAndMessages(PLAYBACK_TIMER_TOKEN)
            }
        )
    }

    // private
    private fun startPlayer() {
        _playerState.value = PlayerState.Playing
        startPlayerUseCase.startPlayer()
        threadTime()
    }

    fun pausePlayer() {
        pausePlayerUseCase.pausePlayer()
        _playerState.value = PlayerState.Paused
        //remove by token
        handler.removeCallbacksAndMessages(PLAYBACK_TIMER_TOKEN)
    }

    //postAtTime + Token
    private fun threadTime() {
        var runnable = object : Runnable {
            override fun run() {
                getCurrentTimeUseCase.getTime()?.let {
                    _playbackTime.postValue(
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(it)
                    )
                    val postTime = SystemClock.uptimeMillis() + Const.DELAY_TIME
                    handler.postAtTime(this, PLAYBACK_TIMER_TOKEN, postTime)
                }
            }
        }
        handler.post(runnable)
    }

    fun playbackControl() {
        playBackControlUseCase.playBackControl()
        when (getCurrentStateUseCase.getCurrentState()) {
            PlayerState.Playing -> {
                _playerState.value = PlayerState.Playing
                startPlayer()
            }

            is PlayerState.Prepared, PlayerState.Paused -> {
                _playerState.value = PlayerState.Paused
                handler.removeCallbacksAndMessages(PLAYBACK_TIMER_TOKEN)
            }

            PlayerState.Completed -> {
                _playerState.value = PlayerState.Completed
                handler.removeCallbacksAndMessages(PLAYBACK_TIMER_TOKEN)
            }

            else -> {}
        }
    }

    public override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        handler.removeCallbacksAndMessages(PLAYBACK_TIMER_TOKEN)
    }

    companion object {
        val PLAYBACK_TIMER_TOKEN = Any()
    }
}



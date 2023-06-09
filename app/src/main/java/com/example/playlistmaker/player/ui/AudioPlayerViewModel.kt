package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(private val audioPlayerRepository: AudioPlayerRepository) : ViewModel() {

    private val _playbackState = MutableLiveData<PlaybackState>()
    val playbackState: LiveData<PlaybackState> = _playbackState

    private val _playbackTime = MutableLiveData<String?>()
    val playbackTime: LiveData<String?> = _playbackTime

    private val _playbackTimeEnd = MutableLiveData<String?>()
    val playbackTimeEnd: LiveData<String?> = _playbackTimeEnd

    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler = Handler(Looper.getMainLooper())

    fun preparePlayer(track: Track) {
        audioPlayerRepository.preparePlayer(track)
        _playbackState.value = PlaybackState.Prepared(track)

        audioPlayerRepository.setOnCompletionListener {
            _playbackState.value = PlaybackState.Completed
            _playbackTimeEnd.value= Const.DEFAULT_TIME
            handler.removeCallbacksAndMessages(threadTime())

        }
    }

    fun startPlayer() {
        _playbackState.value = PlaybackState.Playing
        audioPlayerRepository.startPlayer()
        threadTime()
    }

    fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
        _playbackState.value=PlaybackState.Paused
    }

    private fun threadTime() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                audioPlayerRepository.getCurrentTime()?.let {
                    try {
                        _playbackTime.postValue(SimpleDateFormat("mm:ss", Locale.getDefault()).format(it))
                        handler.postDelayed(this, Const.DELAY_TIME)
                    } catch (e: Exception) {
                        _playbackTime.postValue(PlaybackState.Error(e.message ?: "Error occurred during playback").toString())
                    }
                }
            }
        }, 0)
    }

    fun playbackControl() {
        audioPlayerRepository.playbackControl()
        when (audioPlayerRepository.getCurrentState()) {
            PlayerState.STATE_PLAYING -> {
                _playbackState.value = PlaybackState.Playing
                threadTime()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                _playbackState.value = PlaybackState.Paused
                handler.removeCallbacksAndMessages(threadTime())
            }
            PlayerState.STATE_COMPLETED -> {
                _playbackState.value = PlaybackState.Completed
                handler.removeCallbacksAndMessages(threadTime())
                _playbackTimeEnd.postValue(Const.DEFAULT_TIME)


            }
            else -> {}
        }
    }

    public override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        handler.removeCallbacks{threadTime()}    }

    sealed class PlaybackState {
        object Idle : PlaybackState()
        data class Prepared(val track: Track) : PlaybackState()
        object Playing : PlaybackState()
        object Paused : PlaybackState()
        data class Progress(val currentPosition: Int) : PlaybackState()
        object Completed : PlaybackState()
        data class Error(val errorMessage: String) : PlaybackState()
    }
}
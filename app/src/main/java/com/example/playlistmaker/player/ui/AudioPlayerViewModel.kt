package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.usecase.DeleteTrackUseCase
import com.example.playlistmaker.media.domain.usecase.GetFavoriteIdsUseCase
import com.example.playlistmaker.media.domain.usecase.InsertTrackUseCase
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.toTrackEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(
    private val getCurrentStateUseCase: GetCurrentStateUseCase,
    private val pausePlayerUseCase: PausePlayerUseCase,
    private val playBackControlUseCase: PlayBackControlUseCase,
    private val prepareUseCase: PrepareUseCase,
    private val startPlayerUseCase: StartPlayerUseCase,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,

    private val deleteTrackUseCase: DeleteTrackUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val insertTrackUseCase: InsertTrackUseCase,

    ) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _playbackTime = MutableLiveData<String?>()
    val playbackTime: LiveData<String?> = _playbackTime

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: MutableLiveData<Boolean> = _isFavorite

    private var mediaPlayer: MediaPlayer? = null

    private var timerJob: Job? = null

    fun preparePlayer(track: Track) {
        viewModelScope.launch {
            val favoriteTrackIds = getFavoriteIdsUseCase.getFavoriteIds()
            track.isFavorite = favoriteTrackIds.contains(track.trackId)
            isFavorite.postValue(track.isFavorite)
        }
        prepareUseCase.prepare(
            track = track,
            onPrepared = { _playerState.postValue(PlayerState.Prepared(track)) },
            onComplete = {
                _playerState.postValue(PlayerState.Completed)
                timerJob?.cancel()
            }
        )
    }

    private fun startPlayer() {
        _playerState.value = PlayerState.Playing
        startPlayerUseCase.startPlayer()
        threadTime()
    }

    fun pausePlayer() {
        pausePlayerUseCase.pausePlayer()
        _playerState.value = PlayerState.Paused
        //remove by token
        timerJob?.cancel()
    }


    private fun threadTime() {
        timerJob = viewModelScope.launch {
            while (true) {
                getCurrentTimeUseCase.getTime()?.let {
                    _playbackTime.postValue(
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(it)
                    )
                }
                delay(Const.DELAY_TIME)
            }
        }
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
            }

            PlayerState.Completed -> {
                _playerState.value = PlayerState.Completed
                timerJob?.cancel()
            }

            else -> {}
        }
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                deleteTrackUseCase.deleteTrack(trackEntity = track.toTrackEntity())
            } else {
                insertTrackUseCase.insertTrack(trackEntity = track.toTrackEntity())
            }
            track.isFavorite = !track.isFavorite
            isFavorite.postValue(track.isFavorite)
        }
    }



    public override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        timerJob?.cancel()
    }

    companion object {
        val PLAYBACK_TIMER_TOKEN = Any()
    }
}







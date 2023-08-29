package com.example.playlistmaker.player.ui

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.addPlayList.domain.usecase.GetAllPlaylistToListUseCase
import com.example.playlistmaker.media.domain.usecase.RemoveTrackFromFavouriteUseCase
import com.example.playlistmaker.media.domain.usecase.GetFavoriteIdsUseCase
import com.example.playlistmaker.media.domain.usecase.AddTrackToFavouriteUseCase
import com.example.playlistmaker.media.ui.PlaylistState
import com.example.playlistmaker.player.domain.models.PlayerState
import com.example.playlistmaker.player.domain.usecase.*
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.utility.toTrackEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val removeTrackFromFavouriteUseCase: RemoveTrackFromFavouriteUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val addTrackToFavouriteUseCase: AddTrackToFavouriteUseCase,
    private val getAllPlaylistToListUseCase: GetAllPlaylistToListUseCase,

    ) : ViewModel() {

    private val _playerState = MutableLiveData<PlayerState>()
    val playerState: LiveData<PlayerState> = _playerState

    private val _playbackTime = MutableLiveData<String?>()
    val playbackTime: LiveData<String?> = _playbackTime

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var mediaPlayer: MediaPlayer? = null

    private var timerJob: Job? = null

    private val _state = MutableStateFlow<PlaylistState>(PlaylistState.Empty)
    val state: StateFlow<PlaylistState> = _state

    fun getAllPlaylist() {
        viewModelScope.launch {
            getAllPlaylistToListUseCase.getAllTracks().collect { tracks ->
                if (tracks.isNotEmpty()) {
                    _state.value= PlaylistState.PlaylistLoaded(tracks)
                } else {
                    _state.value= PlaylistState.Empty
                }
            }
        }

    }

    fun preparePlayer(track: Track) {
        viewModelScope.launch {
            getFavoriteIdsUseCase.getFavoriteIds().collect(){
                track.isFavorite = it.contains(track.trackId)
                _isFavorite.postValue(track.isFavorite)            }
            /*track.isFavorite = favoriteTrackIds.contains(track.trackId)
            _isFavorite.postValue(track.isFavorite)*/
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
                removeTrackFromFavouriteUseCase.removeTrack(trackEntity = track.toTrackEntity())
            } else {
                addTrackToFavouriteUseCase.addTrack(trackEntity = track.toTrackEntity())
            }
            track.isFavorite = !track.isFavorite
            _isFavorite.postValue(track.isFavorite)
        }
    }



    public override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        timerJob?.cancel()
    }

}







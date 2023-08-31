package com.example.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.usecase.GetAllPlaylistToListUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val getAllPlaylistToListUseCase: GetAllPlaylistToListUseCase
    ) : ViewModel() {

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

}

sealed class PlaylistState {
    object Empty : PlaylistState()
    data class PlaylistLoaded(val tracks: List<PlaylistEntity>) : PlaylistState()
}
package com.example.playlistmaker.media.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.example.playlistmaker.databinding.FragmentMediatekaBinding
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.usecase.GetAllTracksUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val getAllTracksUseCase: GetAllTracksUseCase) : ViewModel() {
    // Реализация ViewModel для фрагмента избранных треков

    private val _state = MutableLiveData<FavoriteTracksState>()
    val state: LiveData<FavoriteTracksState> = _state

    fun getAllTracks() {
        viewModelScope.launch {
            getAllTracksUseCase.getAllTracks().collect { tracks ->
                _state.postValue(
                    if (tracks.isNotEmpty()) {
                        FavoriteTracksState.TracksLoaded(tracks)
                    } else {
                        FavoriteTracksState.Empty
                    }
                )
            }
        }

    }
}

sealed class FavoriteTracksState {
    object Empty : FavoriteTracksState()
    data class TracksLoaded(val tracks: List<TrackEntity>) : FavoriteTracksState()
}
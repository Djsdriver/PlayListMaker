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

    private val _state = MutableLiveData<FavoriteTracksState>()
    val state: LiveData<FavoriteTracksState> = _state


    fun getAllTracks() {
        viewModelScope.launch {
            getAllTracksUseCase.getAllTracks().collect { tracks ->
                    if (tracks.isNotEmpty()) {
                        _state.postValue(FavoriteTracksState.TracksLoaded(tracks))
                    } else {
                        _state.postValue(FavoriteTracksState.Empty)
                    }
            }
        }

    }

}


sealed class FavoriteTracksState {
    object Empty : FavoriteTracksState()
    data class TracksLoaded(val tracks: List<TrackEntity>) : FavoriteTracksState()
}
package com.example.playlistmaker.media.playlistcontent.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.usecase.DeletePlaylistUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.UpdatePlaylistUseCase
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.media.playlistcontent.domain.GetPlaylistByIdUsecase
import com.example.playlistmaker.media.playlistcontent.domain.usecase.RemoveTrackFromPlaylistUsecase
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utility.toPlaylistModel
import com.example.playlistmaker.utility.toTrack
import com.example.playlistmaker.utility.toTrackEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistContentFragmentViewModel(
    private val getPlaylistByIdUsecase: GetPlaylistByIdUsecase,
    private val removeTrackFromPlaylistUsecase: RemoveTrackFromPlaylistUsecase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
):
    ViewModel() {

    private val _stateDuration = MutableLiveData<String>()
    val stateDuration: LiveData<String> = _stateDuration

    private val _stateCountTracks = MutableLiveData<String>()
    val stateCountTracks: LiveData<String> = _stateCountTracks


    private fun updatePlaylistDuration(playlist: PlaylistModel) {
        val durationSum = playlist.tracks.sumBy { it.trackTimeMillis.toIntOrNull() ?: 0 }
        val format = SimpleDateFormat("mm", Locale.getDefault())
        val formattedDuration = format.format(durationSum)
        _stateDuration.postValue(formattedDuration)
    }

    private fun updatePlaylistCountTracks(playlistModel: PlaylistModel){
        val countTracks=playlistModel.tracks.size
        _stateCountTracks.postValue(countTracks.toString())

    }

    fun deletePlaylist(playlist: PlaylistModel) {
        viewModelScope.launch {
            deletePlaylistUseCase.deletePlaylist(playlist) // вызываем метод deletePlaylist из deletePlaylistUseCase
        }
    }


    fun updatePlaylist(playlistModel: PlaylistModel) {
        viewModelScope.launch {
            updatePlaylistUseCase(playlistModel)
            updatePlaylistDuration(playlistModel)
            updatePlaylistCountTracks(playlistModel)

        }
    }

    fun removeTrackFromPlaylist(track: TrackEntity, playlistId: Int) {
        viewModelScope.launch {
            removeTrackFromPlaylistUsecase.invoke(track, playlistId)
        }
    }

}

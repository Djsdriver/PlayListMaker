package com.example.playlistmaker.search.ui

import androidx.lifecycle.*
import com.example.playlistmaker.search.data.network.SearchTrackRepository
import com.example.playlistmaker.utility.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repository.TrackRepository
import com.example.playlistmaker.search.domain.usecase.AddTrackToHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.ClearHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.LoadDataUseCase
import com.example.playlistmaker.search.domain.usecase.SaveDataUseCase
import com.example.playlistmaker.utility.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class SearchScreenViewModel(
    private val addTrackToHistoryListUseCase: AddTrackToHistoryListUseCase,
    private val clearHistoryListUseCase: ClearHistoryListUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val saveDataUseCase: SaveDataUseCase,
    private val repository: TrackRepository,
) : ViewModel() {

    private var isClickAllowed = true

    private var latestSearchText: String? = null
    private var searchJob: Job? = null

    private val _tracks = MutableLiveData<Resource<List<Track>>>(Resource.loading(null))
    val tracks: LiveData<Resource<List<Track>>> = _tracks


    fun searchTracks(query: String) {
        viewModelScope.launch {
            _tracks.value = Resource.loading(null)
            repository.searchTracks(query)
                .collect { result ->
                    _tracks.postValue(Resource.success(result.data))
                }
        }
    }


    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(Const.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(Const.CLICK_DEBOUNCE_DELAY)
            searchTracks(changedText)
        }
    }

    fun addTrack(track: Track) {
        addTrackToHistoryListUseCase.execute(track)
    }

    fun clearHistoryList() {
        clearHistoryListUseCase.execute()
    }

    fun loadData(): ArrayList<Track> {
        return loadDataUseCase.execute()
    }

    fun saveData(track: Track) {
        saveDataUseCase.execute(track)
    }


}
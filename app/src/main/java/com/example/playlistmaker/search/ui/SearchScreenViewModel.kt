package com.example.playlistmaker.search.ui

import androidx.lifecycle.*
import com.example.playlistmaker.search.data.network.SearchTrackRepository
import com.example.playlistmaker.utility.Resource
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.usecase.AddTrackToHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.ClearHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.LoadDataUseCase
import com.example.playlistmaker.search.domain.usecase.SaveDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SearchScreenViewModel(
    private val addTrackToHistoryListUseCase: AddTrackToHistoryListUseCase,
    private val clearHistoryListUseCase: ClearHistoryListUseCase,
    private val loadDataUseCase: LoadDataUseCase,
    private val saveDataUseCase: SaveDataUseCase,
    private val repository: SearchTrackRepository,
) : ViewModel() {

    private val _tracks = MutableLiveData<Resource<List<Track>>>()
    val tracks: LiveData<Resource<List<Track>>> = _tracks

    fun searchTracks(query: String) {
        viewModelScope.launch(Dispatchers.IO) { // switch to IO thread
            _tracks.postValue(Resource.loading(null))
            val result = repository.searchTracks(query)
            _tracks.postValue(result)
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
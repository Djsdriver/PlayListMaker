package com.example.playlistmaker.search.ui


import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Const
import com.example.playlistmaker.search.data.TrackStorageImpl
import com.example.playlistmaker.search.domain.usecase.AddTrackToHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.ClearHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.LoadDataUseCase
import com.example.playlistmaker.search.domain.usecase.SaveDataUseCase

class SearchScreenViewModelFactory(private  val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchScreenViewModel::class.java)) {
            val sharedPreferences =
                context.getSharedPreferences(Const.SHARED_PREFERENCES_HISTORY_LIST, MODE_PRIVATE)
            val trackStorageImpl = TrackStorageImpl(sharedPreferences)
            val addTrackToHistoryListUseCase = AddTrackToHistoryListUseCase(trackStorageImpl)
            val clearHistoryListUseCase = ClearHistoryListUseCase(trackStorageImpl)
            val loadDataUseCase = LoadDataUseCase(trackStorageImpl)
            val saveDataUseCase = SaveDataUseCase(trackStorageImpl)
            @Suppress("UNCHECKED_CAST")
            return SearchScreenViewModel(
                addTrackToHistoryListUseCase,
                clearHistoryListUseCase,
                loadDataUseCase,
                saveDataUseCase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
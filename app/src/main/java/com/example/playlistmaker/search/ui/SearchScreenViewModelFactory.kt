package com.example.playlistmaker.search.ui


import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.utility.Const
import com.example.playlistmaker.search.data.network.SearchTrackRepository
import com.example.playlistmaker.search.data.network.TrackApi
import com.example.playlistmaker.search.data.storage.TrackStorageImpl
import com.example.playlistmaker.search.domain.usecase.AddTrackToHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.ClearHistoryListUseCase
import com.example.playlistmaker.search.domain.usecase.LoadDataUseCase
import com.example.playlistmaker.search.domain.usecase.SaveDataUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchScreenViewModelFactory(private  val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchScreenViewModel::class.java)) {

            val trackApi= Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TrackApi::class.java)

            val searchTrackRepository= SearchTrackRepository(trackApi = trackApi)

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
                saveDataUseCase,
                searchTrackRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
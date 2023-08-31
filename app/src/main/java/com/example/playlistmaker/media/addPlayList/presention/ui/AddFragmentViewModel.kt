package com.example.playlistmaker.media.addPlayList.presention.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import kotlinx.coroutines.launch

class AddFragmentViewModel(private val insertPlaylistToDatabaseUseCase: InsertPlayListToDatabaseUseCase): ViewModel() {


    fun insertPlaylistToDatabase(playlist: PlaylistEntity) {
        viewModelScope.launch {
            insertPlaylistToDatabaseUseCase.invoke(playlist)
        }
    }

    fun generateImageNameForStorage(): String {
        return "cover_${System.currentTimeMillis()}.jpg"
    }
}
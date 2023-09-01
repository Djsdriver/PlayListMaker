package com.example.playlistmaker.media.addPlayList.presention.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.SaveImageToPrivateStorageUseCase
import kotlinx.coroutines.launch

class AddFragmentViewModel(
    private val insertPlaylistToDatabaseUseCase: InsertPlayListToDatabaseUseCase,
    private val saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase
): ViewModel() {


    fun insertPlaylistToDatabase(playlist: PlaylistEntity) {
        viewModelScope.launch {
            insertPlaylistToDatabaseUseCase.invoke(playlist)
        }
    }

    fun generateImageNameForStorage(): String {
        return "cover_${System.currentTimeMillis()}.jpg"
    }

    fun saveImageToPrivateStorage(uri: Uri, nameOfImage: String) {
        viewModelScope.launch {
            saveImageToPrivateStorageUseCase.invoke(uri, nameOfImage)
            // Обработка полученного пути к изображению, если необходимо
        }
    }
}
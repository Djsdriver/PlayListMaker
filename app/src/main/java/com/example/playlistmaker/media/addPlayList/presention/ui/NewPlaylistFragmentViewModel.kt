package com.example.playlistmaker.media.addPlayList.presention.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.SaveImageToPrivateStorageUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistFragmentViewModel(
    private val insertPlaylistToDatabaseUseCase: InsertPlayListToDatabaseUseCase,
    private val saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase
): ViewModel() {
    private val _uriImage: MutableLiveData<Uri?> = MutableLiveData()
    val uriImage: LiveData<Uri?> get() = _uriImage
    fun setUriImage(uri: Uri?) {
        _uriImage.value = uri
    }

    val generationName=generateImageNameForStorage()

    fun insertPlaylistToDatabase(playlist: PlaylistEntity) {
        viewModelScope.launch {
            insertPlaylistToDatabaseUseCase.invoke(playlist)
        }
    }

    fun generateImageNameForStorage(): String {
        return "cover_${System.currentTimeMillis()}.jpg"
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            saveImageToPrivateStorageUseCase.invoke(uri, generationName)
        }
    }
    fun addPlaylistToDatabase(
        name: String,
        description: String,
    ) {
        if (uriImage.value == null) {
            insertPlaylistToDatabase(
                PlaylistEntity(
                    name = name,
                    description = description,
                    imagePath = generationName,
                    tracks = mutableListOf(),
                    trackCount = 0
                )
            )
        } else {
            saveImageToPrivateStorage(uriImage.value!! )
            insertPlaylistToDatabase(
                PlaylistEntity(
                    name = name,
                    description = description,
                    imagePath = generationName,
                    tracks = mutableListOf(),
                    trackCount = 0
                )
            )
        }
    }
}
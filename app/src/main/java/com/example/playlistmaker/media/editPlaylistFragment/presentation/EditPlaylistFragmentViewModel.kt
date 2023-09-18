package com.example.playlistmaker.media.editPlaylistFragment.presentation

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.usecase.CreateNewPlaylistUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.DeleteImageFromStorageUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.media.playlistcontent.domain.GetPlaylistByIdUsecase
import com.example.playlistmaker.utility.toPlaylistEntity
import com.example.playlistmaker.utility.toPlaylistModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPlaylistFragmentViewModel(
    private val insertPlaylistToDatabaseUseCase: InsertPlayListToDatabaseUseCase,
    private val saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase,
    private val createNewPlaylistUseCase: CreateNewPlaylistUseCase,
    private val getPlaylistByIdUsecase: GetPlaylistByIdUsecase,
    private val deleteImageFromStorageUseCase: DeleteImageFromStorageUseCase
) : ViewModel() {

    private val _uriImage: MutableLiveData<Uri?> = MutableLiveData()
    val uriImage: LiveData<Uri?> get() = _uriImage

    fun setUriImage(uri: Uri?) {
        _uriImage.value = uri
    }

    fun insertPlaylistToDatabase(playlist: PlaylistEntity) {
        viewModelScope.launch {
            insertPlaylistToDatabaseUseCase.invoke(playlist)
        }
    }

    fun deleteImageFromStorage(imagePath: String?) {
        viewModelScope.launch {
            deleteImageFromStorageUseCase.invoke(imagePath)
        }
    }

    private fun generateImageNameForStorage(): String {
        return "cover_${System.currentTimeMillis()}.jpg"
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageName = generateImageNameForStorage()
            saveImageToPrivateStorageUseCase.invoke(uri, imageName)

        }
    }

    fun editPlaylist(
        name: String,
        description: String,
        playlistModel: PlaylistModel?,
        uriImage: Uri?,
        navigateToPlaylistContent: (PlaylistModel) -> Unit,
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val updatedPlaylist = playlistModel?.copy(
                name = name,
                description = description
            )

            val imagePath = if (uriImage != null) {
                saveImageToPrivateStorage(uriImage )// Сохраняем новую картинку
                generateImageNameForStorage() // Путь к новой сохраненной картинке
            } else {
                updatedPlaylist?.imagePath ?: "" // Используем существующий путь к изображению
            }

            updatedPlaylist?.let { playlist ->
                val playlistEntity = playlist.toPlaylistEntity().copy(imagePath = imagePath)
                insertPlaylistToDatabase(playlistEntity) // Вставляем обновленный плейлист в базу данных
                navigateToPlaylistContent(playlist.copy(imagePath = imagePath)) // Навигируем с обновленным плейлистом
            }
        }
    }
    }


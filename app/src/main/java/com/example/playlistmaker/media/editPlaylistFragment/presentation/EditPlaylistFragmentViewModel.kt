package com.example.playlistmaker.media.editPlaylistFragment.presentation

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.addPlayList.domain.usecase.CreateNewPlaylistUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.DeleteImageFromStorageUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.EditPlaylistUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.EditUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.InsertPlayListToDatabaseUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.SaveImageToPrivateStorageUseCase
import com.example.playlistmaker.media.addPlayList.domain.usecase.UpdatePlaylistUseCase
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.media.playlistcontent.domain.GetPlaylistByIdUsecase
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utility.toPlaylistEntity
import com.example.playlistmaker.utility.toPlaylistModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditPlaylistFragmentViewModel(
    private val insertPlaylistToDatabaseUseCase: InsertPlayListToDatabaseUseCase,
    private val saveImageToPrivateStorageUseCase: SaveImageToPrivateStorageUseCase,
    private val createNewPlaylistUseCase: CreateNewPlaylistUseCase,
    private val getPlaylistByIdUsecase: GetPlaylistByIdUsecase,
    private val deleteImageFromStorageUseCase: DeleteImageFromStorageUseCase,
    private val editPlaylistUseCase: EditPlaylistUseCase,
    private val updatePlaylistUseCase: UpdatePlaylistUseCase,
    private val editUseCase: EditUseCase
) : ViewModel() {

    private val _uriImage: MutableLiveData<Uri?> = MutableLiveData()
    val uriImage: LiveData<Uri?> get() = _uriImage
    fun setUriImage(uri: Uri?) {
        _uriImage.value = uri
    }

    val generationName = generateImageNameForStorage()
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
            val isSuccess =saveImageToPrivateStorageUseCase.invoke(uri, generationName)
            Log.d("isSuccess" , isSuccess.toBoolean().toString())
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
                saveImageToPrivateStorage(uriImage)
                generationName
            } else {
                updatedPlaylist?.imagePath ?: ""
            }


            updatedPlaylist?.let { playlist ->
                val playlistEntity = playlist.toPlaylistEntity().copy(imagePath = imagePath)
                insertPlaylistToDatabase(playlistEntity)
                // Вставляем обновленный плейлист в базу данных
                delay(300)
                navigateToPlaylistContent(playlist.copy(imagePath = imagePath)) // Навигируем с обновленным плейлистом
            }
        }

    }
}



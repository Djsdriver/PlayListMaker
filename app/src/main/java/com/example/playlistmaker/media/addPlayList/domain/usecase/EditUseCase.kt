package com.example.playlistmaker.media.addPlayList.domain.usecase

import android.net.Uri
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EditUseCase(private val playlistRepository: PlaylistRepository) {

    suspend fun editPlaylist(playlistId: Int, playlistName: String, playlistDescription: String, imageUri: Uri?, nameImage: String) {

        withContext(Dispatchers.IO) {
            playlistRepository.updatePlaylist(playlistId, playlistName, playlistDescription, imageUri, nameImage)
        }
    }
}
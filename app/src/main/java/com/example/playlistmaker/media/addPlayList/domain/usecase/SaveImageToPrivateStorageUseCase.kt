package com.example.playlistmaker.media.addPlayList.domain.usecase

import android.net.Uri
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository

class SaveImageToPrivateStorageUseCase(private val playlistRepository: PlaylistRepository) {
    suspend operator fun invoke(uri: Uri, nameOfImage: String): String {
        return playlistRepository.saveImageToPrivateStorage(uri, nameOfImage)
    }
}
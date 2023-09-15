package com.example.playlistmaker.media.addPlayList.data.repository


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.playlistmaker.media.addPlayList.data.db.AppDatabasePlayList
import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.media.addPlayList.domain.repository.PlaylistRepository
import com.example.playlistmaker.utility.toPlaylistEntity
import com.example.playlistmaker.utility.toPlaylistModel
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(private val appDatabase: AppDatabasePlayList, private val context: Context): PlaylistRepository {
    override fun getAllPlaylists(): Flow<List<PlaylistEntity>> = appDatabase.getPlaylistDao().getAllPlaylists()


    override suspend fun insertPlaylist(playlist: PlaylistEntity) {
        appDatabase.getPlaylistDao().insertPlaylist(playlist)
    }

    override suspend fun createPlaylist(name: String, description: String, imagePath: String?) {
        val playlist = PlaylistEntity(
            name = name,
            description = description,
            imagePath = imagePath ?: "",
            tracks = mutableListOf(),
            trackCount = 0
        )
        insertPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistModel: PlaylistModel) {
        appDatabase.getPlaylistDao().deletePlaylist(playlistModel.toPlaylistEntity())
    }


    override suspend fun updatePlaylist(playlistModel: PlaylistModel) {
        appDatabase.getPlaylistDao().updatePlaylist(playlistModel.toPlaylistEntity())
    }

    override suspend fun saveImageToPrivateStorage(uri: Uri, nameOfImage: String): String {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"my_album")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, nameOfImage)
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return  file.absolutePath
    }

    override suspend fun deleteImageFromStorage(imagePath: String?) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"my_album")
        Log.d("PlaylistRepositoryImpl", "Deleting image at path: $imagePath")
        if (imagePath != null && imagePath.isNotEmpty()) {
            val file = File(filePath,imagePath)
            if (file.exists()) {
                file.delete()
                Log.d("PlaylistRepositoryImpl", "Image deleted successfully")
            } else {
                Log.d("PlaylistRepositoryImpl", "Image not found at path")
            }
        } else {
            Log.d("PlaylistRepositoryImpl", "Invalid image path")
        }
    }



}
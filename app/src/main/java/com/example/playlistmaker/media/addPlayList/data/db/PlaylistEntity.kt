package com.example.playlistmaker.media.addPlayList.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.media.data.db.TrackEntity

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val imagePath: String,
    var tracks: MutableList<TrackEntity> = mutableListOf(),
    val trackCount: Int
)
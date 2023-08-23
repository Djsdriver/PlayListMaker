package com.example.playlistmaker

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long ,
    val title: String,
    val description: String,
    val coverImagePath: String,
    val trackIds: List<Long>,
    val trackCount: Int
)
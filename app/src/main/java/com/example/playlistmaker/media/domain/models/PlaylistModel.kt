package com.example.playlistmaker.media.domain.models

import androidx.room.PrimaryKey
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

data class PlaylistModel(
    val id: Int = 0,
    val name: String,
    val description: String,
    val imagePath: String,
    var tracksId: MutableList<Track> = mutableListOf(),
    val trackCount: Int
)

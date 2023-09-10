package com.example.playlistmaker.media.domain.models

import com.example.playlistmaker.search.domain.models.Track
import java.io.Serializable

data class PlaylistModel(
    val id: Int = 0,
    val name: String,
    val description: String,
    val imagePath: String,
    var tracks: MutableList<Track> = mutableListOf(),
    val trackCount: Int
):Serializable

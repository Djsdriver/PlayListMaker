package com.example.playlistmaker.media.addPlayList.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val imagePath: String,
    var tracksId: MutableList<TrackEntity> = mutableListOf() ,
    val trackCount: Int
)
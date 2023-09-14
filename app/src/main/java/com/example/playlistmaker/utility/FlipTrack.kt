package com.example.playlistmaker.utility

import com.example.playlistmaker.media.addPlayList.data.db.PlaylistEntity
import com.example.playlistmaker.media.domain.models.PlaylistModel
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import java.util.Date

fun Track.toTrackEntity(): TrackEntity {
    return TrackEntity(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl60 = this.artworkUrl60,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl,
        isFavorite = this.isFavorite,
        timeAdded= Date().time
    )
}

fun TrackEntity.toTrack(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTimeMillis = this.trackTimeMillis,
        artworkUrl60 = this.artworkUrl60,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl,
        isFavorite = this.isFavorite,
        timeAdded= Date().time
    )
}

fun PlaylistModel.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id= this.id,
        name= this.name,
        description= this.description,
        imagePath= this.imagePath,
        tracks = this.tracks.map { track -> track.toTrackEntity() }.toMutableList(),
        trackCount = this.trackCount
    )
}

fun PlaylistEntity.toPlaylistModel(): PlaylistModel {
    return PlaylistModel(
        id= this.id,
        name= this.name,
        description= this.description,
        imagePath= this.imagePath,
        tracks=this.tracks.map { track -> track.toTrack() }.toMutableList(),
        trackCount=this.trackCount

    )
}
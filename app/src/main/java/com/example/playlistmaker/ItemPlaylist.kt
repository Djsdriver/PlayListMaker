package com.example.playlistmaker

data class ItemPlaylist(
    val playlistId: Long?,
    val name: String,
    val description: String,
    val playlistImagePath: String,
    var tracksQuantity: Int = 0
)

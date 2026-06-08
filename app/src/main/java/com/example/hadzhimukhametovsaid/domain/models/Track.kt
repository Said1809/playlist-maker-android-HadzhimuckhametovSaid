package com.example.hadzhimukhametovsaid.domain.models

data class Track(
    val id: Long = 0,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String = "",
    val favorite: Boolean = false,
    val playlistId: Long = 0
)

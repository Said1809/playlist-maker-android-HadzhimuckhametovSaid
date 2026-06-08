package com.example.hadzhimukhametovsaid.domain.models

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverImageUri: String? = null,
    val tracks: List<Track>
)

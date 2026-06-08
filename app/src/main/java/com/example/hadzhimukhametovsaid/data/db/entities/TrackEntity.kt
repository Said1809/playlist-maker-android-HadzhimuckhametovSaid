package com.example.hadzhimukhametovsaid.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val favorite: Boolean,
    val playlistId: Long
)

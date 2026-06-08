package com.example.hadzhimukhametovsaid.domain.api

import com.example.hadzhimukhametovsaid.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getPlaylist(playlistId: Long): Flow<Playlist?>

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String? = null)

    suspend fun deletePlaylistById(id: Long)
}

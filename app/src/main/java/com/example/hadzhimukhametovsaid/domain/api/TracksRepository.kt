package com.example.hadzhimukhametovsaid.domain.api

import com.example.hadzhimukhametovsaid.domain.models.SearchResult
import com.example.hadzhimukhametovsaid.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun searchTracks(expression: String): SearchResult

    fun getTrackByNameAndArtist(track: Track): Flow<Track?>

    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)

    suspend fun deleteTrackFromPlaylist(track: Track)

    suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean)

    fun deleteTracksByPlaylistId(playlistId: Long)
}

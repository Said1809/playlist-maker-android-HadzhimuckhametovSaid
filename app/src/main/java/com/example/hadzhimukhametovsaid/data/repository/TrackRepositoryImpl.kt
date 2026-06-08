package com.example.hadzhimukhametovsaid.data.repository

import com.example.hadzhimukhametovsaid.data.db.AppDatabase
import com.example.hadzhimukhametovsaid.data.db.entities.TrackEntity
import com.example.hadzhimukhametovsaid.data.dto.TracksSearchRequest
import com.example.hadzhimukhametovsaid.data.dto.TracksSearchResponse
import com.example.hadzhimukhametovsaid.domain.api.NetworkClient
import com.example.hadzhimukhametovsaid.domain.api.TracksRepository
import com.example.hadzhimukhametovsaid.domain.models.SearchResult
import com.example.hadzhimukhametovsaid.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val database: AppDatabase
) : TracksRepository {

    override suspend fun searchTracks(expression: String): SearchResult {
        return try {
            val response = networkClient.doRequest(TracksSearchRequest(expression))
            when (response.resultCode) {
                200 -> {
                    val favoriteTracksIds = database.trackDao().getFavoriteTracksIds().toSet()
                    val responseDto = response as TracksSearchResponse
                    val tracks = responseDto.results?.map {
                        Track(
                            id = it.trackId ?: 0L,
                            trackName = it.trackName ?: "",
                            artistName = it.artistName ?: "",
                            trackTime = try {
                                SimpleDateFormat("mm:ss", Locale.getDefault()).format(it.trackTimeMillis ?: 0)
                            } catch (e: Exception) {
                                "00:00"
                            },
                            artworkUrl100 = it.artworkUrl100 ?: "",
                            favorite = favoriteTracksIds.contains(it.trackId ?: -1L)
                        )
                    } ?: emptyList()
                    SearchResult.Success(tracks)
                }
                -1 -> SearchResult.InternetError
                else -> SearchResult.Error("Error code: ${response.resultCode}")
            }
        } catch (e: Exception) {
            SearchResult.Error(e.message ?: "Unknown error")
        }
    }

    override fun getTrackByNameAndArtist(track: Track): Flow<Track?> {
        return kotlinx.coroutines.flow.flow {
            val entity = database.trackDao().getTrackByNameAndArtist(track.trackName, track.artistName)
            emit(entity?.toDomain())
        }
    }

    override suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        database.trackDao().insertTrack(track.toEntity().copy(playlistId = playlistId))
    }

    override suspend fun deleteTrackFromPlaylist(track: Track) {
        database.trackDao().insertTrack(track.toEntity().copy(playlistId = 0))
    }

    override suspend fun updateTrackFavoriteStatus(track: Track, isFavorite: Boolean) {
        database.trackDao().insertTrack(track.toEntity().copy(favorite = isFavorite))
    }

    override fun deleteTracksByPlaylistId(playlistId: Long) {
        // Implementation needed or marked as suspend if required by interface
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return database.trackDao().getFavoriteTracks().map { list ->
            list.map { it.toDomain() }
        }
    }

    private fun TrackEntity.toDomain() = Track(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        artworkUrl100 = artworkUrl100,
        favorite = favorite,
        playlistId = playlistId
    )

    private fun Track.toEntity() = TrackEntity(
        id = id,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        artworkUrl100 = artworkUrl100,
        favorite = favorite,
        playlistId = playlistId
    )
}

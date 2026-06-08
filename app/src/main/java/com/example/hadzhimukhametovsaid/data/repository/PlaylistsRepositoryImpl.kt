package com.example.hadzhimukhametovsaid.data.repository

import com.example.hadzhimukhametovsaid.data.db.dao.PlaylistDao
import com.example.hadzhimukhametovsaid.data.db.dao.TrackDao
import com.example.hadzhimukhametovsaid.data.db.entities.PlaylistEntity
import com.example.hadzhimukhametovsaid.data.db.entities.TrackEntity
import com.example.hadzhimukhametovsaid.data.storage.CoverImageStorage
import com.example.hadzhimukhametovsaid.domain.api.PlaylistsRepository
import com.example.hadzhimukhametovsaid.domain.models.Playlist
import com.example.hadzhimukhametovsaid.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged

class PlaylistsRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val trackDao: TrackDao,
    private val coverImageStorage: CoverImageStorage
) : PlaylistsRepository {

    override fun getPlaylist(playlistId: Long): Flow<Playlist?> {
        return combine(
            playlistDao.getPlaylistById(playlistId).distinctUntilChanged(),
            trackDao.getTracksByPlaylistId(playlistId).distinctUntilChanged()
        ) { playlistEntity, tracks ->
            playlistEntity?.let {
                Playlist(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    coverImageUri = it.coverImageUri,
                    tracks = tracks.map { trackEntity -> trackEntity.toDomain() }
                )
            }
        }.distinctUntilChanged()
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return combine(
            playlistDao.getAllPlaylists().distinctUntilChanged(),
            trackDao.getAllPlaylistTracks().distinctUntilChanged()
        ) { playlists, allTracks ->
            val tracksByPlaylist = allTracks.groupBy { it.playlistId }
            playlists.map { entity ->
                Playlist(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    coverImageUri = entity.coverImageUri,
                    tracks = (tracksByPlaylist[entity.id] ?: emptyList()).map { it.toDomain() }
                )
            }
        }.distinctUntilChanged()
    }

    override suspend fun addNewPlaylist(name: String, description: String, coverImageUri: String?) {
        val playlistId = playlistDao.insertPlaylist(
            PlaylistEntity(
                name = name,
                description = description,
                coverImageUri = null
            )
        )
        if (coverImageUri != null) {
            val localPath = coverImageStorage.copyToLocalStorage(coverImageUri, playlistId)
            if (localPath != null) {
                playlistDao.updateCoverImageUri(playlistId, localPath)
            }
        }
    }

    override suspend fun deletePlaylistById(id: Long) {
        coverImageStorage.deleteCover(id)
        playlistDao.deletePlaylistById(id)
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
}

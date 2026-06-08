package com.example.hadzhimukhametovsaid.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hadzhimukhametovsaid.data.db.entities.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE favorite = 1")
    fun getFavoriteTracks(): Flow<List<TrackEntity>>

    @Query("SELECT id FROM tracks WHERE favorite = 1")
    suspend fun getFavoriteTracksIds(): List<Long>

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrackById(id: Long): TrackEntity?

    @Query("SELECT * FROM tracks WHERE trackName = :name AND artistName = :artist")
    suspend fun getTrackByNameAndArtist(name: String, artist: String): TrackEntity?

    @Query("SELECT * FROM tracks WHERE playlistId = :playlistId")
    fun getTracksByPlaylistId(playlistId: Long): Flow<List<TrackEntity>>

    @Query("SELECT * FROM tracks WHERE playlistId > 0")
    fun getAllPlaylistTracks(): Flow<List<TrackEntity>>

    @Query("DELETE FROM tracks WHERE playlistId = :playlistId")
    suspend fun deleteTracksByPlaylistId(playlistId: Long)
}

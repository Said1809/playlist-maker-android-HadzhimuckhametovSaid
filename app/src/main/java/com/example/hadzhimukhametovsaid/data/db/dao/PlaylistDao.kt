package com.example.hadzhimukhametovsaid.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hadzhimukhametovsaid.data.db.entities.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("UPDATE playlists SET coverImageUri = :uri WHERE id = :id")
    suspend fun updateCoverImageUri(id: Long, uri: String?)

    @Query("SELECT * FROM playlists")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :id")
    fun getPlaylistById(id: Long): Flow<PlaylistEntity?>

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylistById(id: Long)
}

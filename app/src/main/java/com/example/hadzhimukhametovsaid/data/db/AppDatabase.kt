package com.example.hadzhimukhametovsaid.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hadzhimukhametovsaid.data.db.dao.PlaylistDao
import com.example.hadzhimukhametovsaid.data.db.dao.TrackDao
import com.example.hadzhimukhametovsaid.data.db.entities.PlaylistEntity
import com.example.hadzhimukhametovsaid.data.db.entities.TrackEntity

@Database(entities = [TrackEntity::class, PlaylistEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}

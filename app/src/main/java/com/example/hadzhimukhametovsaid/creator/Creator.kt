package com.example.hadzhimukhametovsaid.creator

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.hadzhimukhametovsaid.data.db.AppDatabase
import com.example.hadzhimukhametovsaid.data.network.NetworkClientImpl
import com.example.hadzhimukhametovsaid.data.preferences.SearchHistoryPreferencesImpl
import com.example.hadzhimukhametovsaid.domain.api.SearchHistoryPreferences
import com.example.hadzhimukhametovsaid.data.repository.PlaylistsRepositoryImpl
import com.example.hadzhimukhametovsaid.data.storage.CoverImageStorage
import com.example.hadzhimukhametovsaid.data.repository.SearchHistoryRepositoryImpl
import com.example.hadzhimukhametovsaid.data.repository.TrackRepositoryImpl
import com.example.hadzhimukhametovsaid.domain.api.PlaylistsRepository
import com.example.hadzhimukhametovsaid.domain.api.SearchHistoryRepository
import com.example.hadzhimukhametovsaid.domain.api.TracksRepository

private val Context.searchHistoryDataStore by preferencesDataStore(name = "search_history")
private val Context.settingsDataStore by preferencesDataStore(name = "settings")

object Creator {
    private var database: AppDatabase? = null
    private var appSettingsPreferences: com.example.hadzhimukhametovsaid.data.preferences.AppSettingsPreferences? = null

    private fun getDatabase(context: Context): AppDatabase {
        return database ?: Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "playlist_maker.db"
        ).fallbackToDestructiveMigration()
            .build().also { database = it }
    }

    fun getTracksRepository(context: Context): TracksRepository {
        return TrackRepositoryImpl(NetworkClientImpl(context), getDatabase(context))
    }

    fun getPlaylistsRepository(context: Context): PlaylistsRepository {
        val appContext = context.applicationContext
        val database = getDatabase(appContext)
        return PlaylistsRepositoryImpl(
            playlistDao = database.playlistDao(),
            trackDao = database.trackDao(),
            coverImageStorage = CoverImageStorage(appContext)
        )
    }

    fun getSearchHistoryRepository(context: Context): SearchHistoryRepository {
        val preferences: SearchHistoryPreferences =
            SearchHistoryPreferencesImpl(context.searchHistoryDataStore)
        return SearchHistoryRepositoryImpl(preferences)
    }

    fun getAppSettingsPreferences(context: Context): com.example.hadzhimukhametovsaid.data.preferences.AppSettingsPreferences {
        return appSettingsPreferences ?: com.example.hadzhimukhametovsaid.data.preferences.AppSettingsPreferences(context.settingsDataStore)
            .also { appSettingsPreferences = it }
    }
}

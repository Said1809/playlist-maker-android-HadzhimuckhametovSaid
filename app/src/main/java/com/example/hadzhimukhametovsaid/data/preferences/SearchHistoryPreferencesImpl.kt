package com.example.hadzhimukhametovsaid.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.hadzhimukhametovsaid.domain.api.SearchHistoryPreferences
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchHistoryPreferencesImpl(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope = CoroutineScope(CoroutineName("search-history-preferences") + SupervisorJob())
) : SearchHistoryPreferences {
    private val preferencesKey = stringPreferencesKey("search_history")

    override fun addEntry(word: String) {
        if (word.isBlank()) {
            return
        }

        coroutineScope.launch {
            dataStore.edit { preferences ->
                val historyString = preferences[preferencesKey].orEmpty()
                val history = if (historyString.isNotEmpty()) {
                    historyString.split(SEPARATOR).toMutableList()
                } else {
                    mutableListOf()
                }

                history.remove(word)
                history.add(0, word)

                val subList = history.take(MAX_ENTRIES)
                val updatedString = subList.joinToString(SEPARATOR)

                preferences[preferencesKey] = updatedString
            }
        }
    }

    override suspend fun getEntries(): List<String> {
        val historyString = dataStore.data.map { it[preferencesKey] }.first() ?: ""
        return if (historyString.isNotEmpty()) {
            historyString.split(SEPARATOR)
        } else {
            emptyList()
        }
    }

    companion object {
        private const val MAX_ENTRIES = 10
        private const val SEPARATOR = ","
    }
}

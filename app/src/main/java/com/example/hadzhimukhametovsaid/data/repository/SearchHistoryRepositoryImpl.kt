package com.example.hadzhimukhametovsaid.data.repository

import com.example.hadzhimukhametovsaid.domain.api.SearchHistoryPreferences
import com.example.hadzhimukhametovsaid.domain.api.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val preferences: SearchHistoryPreferences
) : SearchHistoryRepository {
    override fun addEntry(word: String) {
        preferences.addEntry(word)
    }

    override suspend fun getEntries(): List<String> {
        return preferences.getEntries()
    }
}

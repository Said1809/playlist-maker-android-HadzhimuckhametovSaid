package com.example.hadzhimukhametovsaid.domain.api

interface SearchHistoryPreferences {
    fun addEntry(word: String)
    suspend fun getEntries(): List<String>
}

package com.example.hadzhimukhametovsaid.domain.models

sealed interface SearchResult {
    data class Success(val tracks: List<Track>) : SearchResult
    data object InternetError : SearchResult
    data class Error(val message: String) : SearchResult
}

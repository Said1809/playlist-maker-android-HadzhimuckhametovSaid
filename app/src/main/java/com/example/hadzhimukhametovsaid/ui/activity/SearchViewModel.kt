package com.example.hadzhimukhametovsaid.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hadzhimukhametovsaid.creator.Creator
import com.example.hadzhimukhametovsaid.domain.api.SearchHistoryRepository
import com.example.hadzhimukhametovsaid.domain.api.TracksRepository
import com.example.hadzhimukhametovsaid.domain.models.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val tracksRepository: TracksRepository = Creator.getTracksRepository(application)
    private val searchHistoryRepository: SearchHistoryRepository = Creator.getSearchHistoryRepository(application)

    private val _searchScreenState = MutableStateFlow<SearchState>(SearchState.Initial)
    val searchScreenState = _searchScreenState.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory = _searchHistory.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadHistory()
    }

    fun search(whatSearch: String) {
        if (whatSearch.isBlank()) {
            _searchScreenState.update { SearchState.Initial }
            return
        }
        
        searchHistoryRepository.addEntry(whatSearch)
        loadHistory()

        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            _searchScreenState.update { SearchState.Searching }
            when (val result = tracksRepository.searchTracks(expression = whatSearch)) {
                is SearchResult.Success -> {
                    _searchScreenState.update { SearchState.Success(foundList = result.tracks) }
                }
                is SearchResult.InternetError -> {
                    _searchScreenState.update { SearchState.InternetError }
                }
                is SearchResult.Error -> {
                    _searchScreenState.update { SearchState.Fail(result.message) }
                }
            }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            _searchHistory.emit(searchHistoryRepository.getEntries())
        }
    }

    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(application) as T
                }
            }
    }
}

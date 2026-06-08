package com.example.hadzhimukhametovsaid.ui.activity

import com.example.hadzhimukhametovsaid.domain.models.Track

sealed class SearchState {
    object Initial : SearchState() // Первоначальное cостояние экрана
    object Searching : SearchState() // Cостояние экрана при начале поиска
    data class Success(val foundList: List<Track>) : SearchState() // Cостояние экрана при успешном завершении поиска
    data class Fail(val error: String) : SearchState() // Cостояние экрана, если при запросе к серверу произошла ошибка
    object InternetError : SearchState()
}

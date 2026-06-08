package com.example.hadzhimukhametovsaid.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hadzhimukhametovsaid.creator.Creator
import com.example.hadzhimukhametovsaid.data.preferences.AppSettingsPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val appSettingsPreferences: AppSettingsPreferences = Creator.getAppSettingsPreferences(application)

    val isDarkTheme: StateFlow<Boolean> = appSettingsPreferences.isDarkTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            appSettingsPreferences.setDarkTheme(isDark)
        }
    }

    companion object {
        fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(application) as T
                }
            }
    }
}

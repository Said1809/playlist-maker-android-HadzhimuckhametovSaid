package com.example.hadzhimukhametovsaid.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hadzhimukhametovsaid.creator.Creator
import com.example.hadzhimukhametovsaid.domain.api.PlaylistsRepository
import com.example.hadzhimukhametovsaid.domain.api.TracksRepository
import com.example.hadzhimukhametovsaid.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlaylistViewModel(
    application: Application,
    private val playlistsRepository: PlaylistsRepository,
    private val tracksRepository: TracksRepository,
    private val playlistId: Long
) : AndroidViewModel(application) {
    val playlist: Flow<Playlist?> = playlistsRepository.getPlaylist(playlistId)

    fun deletePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.deleteTracksByPlaylistId(playlistId)
            playlistsRepository.deletePlaylistById(playlistId)
        }
    }

    fun deleteTrack(track: com.example.hadzhimukhametovsaid.domain.models.Track) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksRepository.deleteTrackFromPlaylist(track)
        }
    }

    companion object {
        fun getViewModelFactory(application: Application, playlistId: Long): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PlaylistViewModel(
                        application,
                        Creator.getPlaylistsRepository(application),
                        Creator.getTracksRepository(application),
                        playlistId
                    ) as T
                }
            }
    }
}

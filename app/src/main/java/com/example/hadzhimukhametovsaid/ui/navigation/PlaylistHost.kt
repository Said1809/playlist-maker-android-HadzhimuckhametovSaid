package com.example.hadzhimukhametovsaid.ui.navigation

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.hadzhimukhametovsaid.domain.models.Track
import com.example.hadzhimukhametovsaid.ui.activity.*

@Composable
fun PlaylistHost(
    navController: NavHostController,
    application: Application,
    searchViewModel: SearchViewModel,
    playlistsViewModel: PlaylistsViewModel
) {
    var selectedTrack by remember { mutableStateOf<Track?>(null) }

    NavHost(
        navController = navController,
        startDestination = Destination.MAIN_SCREEN.name
    ) {
        composable(Destination.MAIN_SCREEN.name) {
            MainScreen(
                onSearchClick = { navController.navigate(Destination.SEARCH_SCREEN.name) },
                onPlaylistsClick = { navController.navigate(Destination.PLAYLISTS_SCREEN.name) },
                onFavoritesClick = { navController.navigate(Destination.FAVORITES_SCREEN.name) },
                onSettingsClick = { navController.navigate(Destination.SETTINGS_SCREEN.name) }
            )
        }

        composable(Destination.SEARCH_SCREEN.name) {
            SearchScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = searchViewModel,
                onTrackClick = { track ->
                    selectedTrack = track
                    navController.navigate(Destination.TRACK_DETAILS_SCREEN.name)
                }
            )
        }

        composable(Destination.PLAYLISTS_SCREEN.name) {
            PlaylistsScreen(
                modifier = Modifier.fillMaxSize(),
                playlistsViewModel = playlistsViewModel,
                addNewPlaylist = { navController.navigate(Destination.NEW_PLAYLIST_SCREEN.name) },
                navigateToPlaylist = { id ->
                    navController.navigate("${Destination.PLAYLIST_SCREEN.name}/$id")
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(Destination.NEW_PLAYLIST_SCREEN.name) {
            NewPlaylistScreen(
                playlistsViewModel = playlistsViewModel,
                onSaveSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = "${Destination.PLAYLIST_SCREEN.name}/{index}",
            arguments = listOf(navArgument("index") { type = NavType.LongType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getLong("index") ?: 0L
            val playlistViewModel: PlaylistViewModel = viewModel(
                factory = PlaylistViewModel.getViewModelFactory(application, playlistId)
            )

            PlaylistScreen(
                modifier = Modifier.fillMaxSize(),
                playlistViewModel = playlistViewModel,
                navigateBack = { navController.popBackStack() },
                onTrackClick = { track ->
                    selectedTrack = track
                    navController.navigate(Destination.TRACK_DETAILS_SCREEN.name)
                }
            )
        }

        composable(Destination.FAVORITES_SCREEN.name) {
            FavoritesScreen(
                playlistsViewModel = playlistsViewModel,
                onTrackClick = { track ->
                    selectedTrack = track
                    navController.navigate(Destination.TRACK_DETAILS_SCREEN.name)
                },
                navigateBack = { navController.popBackStack() }
            )
        }

        composable(Destination.TRACK_DETAILS_SCREEN.name) {
            selectedTrack?.let { track ->
                TrackDetailsScreen(
                    track = track,
                    playlistsViewModel = playlistsViewModel,
                    navigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(Destination.SETTINGS_SCREEN.name) {
            SettingsScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}

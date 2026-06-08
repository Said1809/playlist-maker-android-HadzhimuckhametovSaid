package com.example.hadzhimukhametovsaid.ui.activity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.hadzhimukhametovsaid.R
import com.example.hadzhimukhametovsaid.domain.models.Track
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    playlistsViewModel: PlaylistsViewModel,
    onTrackClick: (Track) -> Unit,
    navigateBack: () -> Unit
) {
    val favoriteList by playlistsViewModel.favoriteList.collectAsStateWithLifecycle(emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.favorites)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (favoriteList.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.favorites_empty))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(
                    items = favoriteList,
                    key = { it.id },
                    contentType = { "track" }
                ) { track ->
                    TrackListItem(
                        track = track,
                        onClick = { onTrackClick(track) },
                        onLongClick = {
                            scope.launch {
                                playlistsViewModel.toggleFavorite(track, false)
                            }
                        }
                    )
                    HorizontalDivider(thickness = 0.5.dp)
                }
            }
        }
    }
}

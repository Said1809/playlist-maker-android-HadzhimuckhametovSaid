package com.example.hadzhimukhametovsaid.ui.activity

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.hadzhimukhametovsaid.R
import com.example.hadzhimukhametovsaid.domain.models.Track

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PlaylistScreen(
    modifier: Modifier,
    playlistViewModel: PlaylistViewModel,
    navigateBack: () -> Unit,
    onTrackClick: (Track) -> Unit
) {
    val playlist by playlistViewModel.playlist.collectAsState(null)
    var showDeletePlaylistDialog by remember { mutableStateOf(false) }
    var trackToDelete by remember { mutableStateOf<Track?>(null) }

    if (showDeletePlaylistDialog) {
        AlertDialog(
            onDismissRequest = { showDeletePlaylistDialog = false },
            title = { Text(stringResource(R.string.delete_playlist)) },
            text = { Text(stringResource(R.string.delete_playlist_confirm, playlist?.name ?: "")) },
            confirmButton = {
                TextButton(onClick = {
                    playlistViewModel.deletePlaylist()
                    showDeletePlaylistDialog = false
                    navigateBack()
                }) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeletePlaylistDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    if (trackToDelete != null) {
        AlertDialog(
            onDismissRequest = { trackToDelete = null },
            title = { Text(stringResource(R.string.delete_track)) },
            text = { Text(stringResource(R.string.delete_track_confirm, trackToDelete?.trackName ?: "")) },
            confirmButton = {
                TextButton(onClick = {
                    trackToDelete?.let { playlistViewModel.deleteTrack(it) }
                    trackToDelete = null
                }) {
                    Text(stringResource(R.string.delete), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { trackToDelete = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(playlist?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDeletePlaylistDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.delete_playlist)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            playlist?.let { pl ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    if (pl.coverImageUri != null) {
                        AsyncImage(
                            model = pl.coverImageUri,
                            contentDescription = stringResource(R.string.playlist_cover),
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_music),
                            contentDescription = stringResource(R.string.playlist_cover),
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pl.name,
                    fontSize = 24.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                
                if (pl.description.isNotBlank()) {
                    Text(
                        text = pl.description,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Text(
                    text = stringResource(R.string.tracks_count, pl.tracks.size),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(pl.tracks) { track ->
                        TrackListItem(
                            track = track,
                            onClick = { onTrackClick(track) },
                            onLongClick = { trackToDelete = track }
                        )
                        HorizontalDivider(thickness = 0.5.dp)
                    }
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

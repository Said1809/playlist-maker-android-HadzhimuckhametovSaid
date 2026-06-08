package com.example.hadzhimukhametovsaid.ui.activity

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.hadzhimukhametovsaid.R
import com.example.hadzhimukhametovsaid.domain.models.Track
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(
    track: Track,
    playlistsViewModel: PlaylistsViewModel,
    navigateBack: () -> Unit
) {
    var isFavorite by remember { mutableStateOf(track.favorite) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val playlists by playlistsViewModel.playlists.collectAsState(emptyList())
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.track_details)) },
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
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = track.artworkUrl100,
                modifier = Modifier.size(240.dp),
                placeholder = painterResource(id = R.drawable.ic_music),
                error = painterResource(id = R.drawable.ic_music),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = track.trackName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = track.artistName, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = track.trackTime, fontSize = 16.sp)

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    isFavorite = !isFavorite
                    scope.launch {
                        playlistsViewModel.toggleFavorite(track, isFavorite)
                    }
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = stringResource(R.string.favorite),
                        tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = stringResource(R.string.add_to_playlist),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        stringResource(R.string.choose_playlist),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    LazyColumn {
                        items(playlists) { playlist ->
                            Text(
                                text = playlist.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        scope.launch {
                                            playlistsViewModel.insertTrackToPlaylist(track, playlist.id)
                                            showBottomSheet = false
                                        }
                                    }
                                    .padding(vertical = 16.dp)
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

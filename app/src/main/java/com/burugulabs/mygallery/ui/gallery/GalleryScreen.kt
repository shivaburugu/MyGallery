package com.burugulabs.mygallery.ui.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.burugulabs.mygallery.data.model.MediaItem
import com.burugulabs.mygallery.ui.viewmodel.GalleryUiState
import com.burugulabs.mygallery.ui.viewmodel.GalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel,
    onImageClick: (MediaItem) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentProfileUri by viewModel.currentProfileUri.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadImages()
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(112.dp)
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Gallery",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 12.dp)
                    )
                    Box(modifier = Modifier.padding(end = 16.dp)) {
                        if (currentProfileUri != null) {
                            AsyncImage(
                                model = currentProfileUri,
                                contentDescription = "Current Profile",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = "No Profile Picture",
                                modifier = Modifier.size(80.dp)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is GalleryUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is GalleryUiState.Success -> {
                    GalleryGrid(
                        images = state.images,
                        onImageClick = onImageClick
                    )
                }
                is GalleryUiState.Error -> {
                    Text(
                        text = "Error: ${state.message}",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is GalleryUiState.Empty -> {
                    Text(
                        text = "No images found.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun GalleryGrid(
    images: List<MediaItem>,
    onImageClick: (MediaItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(images, key = { it.id }) { image ->
            GalleryItem(
                image = image,
                onClick = { onImageClick(image) }
            )
        }
    }
}

@Composable
fun GalleryItem(
    image: MediaItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        AsyncImage(
            model = image.uri,
            contentDescription = image.displayName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

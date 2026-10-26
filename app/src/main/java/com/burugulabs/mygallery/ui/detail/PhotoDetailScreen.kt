package com.burugulabs.mygallery.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.burugulabs.mygallery.ui.viewmodel.PhotoDetailViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoDetailScreen(
    uri: String,
    viewModel: PhotoDetailViewModel,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val currentProfileUri by viewModel.currentProfileUri.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is PhotoDetailViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
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
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = "Photo Detail",
                        style = MaterialTheme.typography.titleLarge,
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
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = uri,
                contentDescription = "Full Screen Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            Button(
                onClick = { viewModel.setAsProfilePicture(uri) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Set as Profile Picture")
            }
        }
    }
}

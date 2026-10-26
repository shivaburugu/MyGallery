package com.burugulabs.mygallery.ui.permissions

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StoragePermissionWrapper(
    content: @Composable () -> Unit
) {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionState = rememberPermissionState(permission)

    if (permissionState.status.isGranted) {
        content()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textToShow = if (permissionState.status.shouldShowRationale) {
                "The gallery needs access to your photos to display them. Please grant the permission."
            } else {
                "Photo access is required for this app to work. Please grant the permission."
            }
            Text(text = textToShow)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }
}

package com.burugulabs.mygallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.burugulabs.mygallery.ui.navigation.MyGalleryNavHost
import com.burugulabs.mygallery.ui.permissions.StoragePermissionWrapper
import com.burugulabs.mygallery.ui.theme.MyGalleryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyGalleryTheme {
                StoragePermissionWrapper {
                    MyGalleryNavHost()
                }
            }
        }
    }
}

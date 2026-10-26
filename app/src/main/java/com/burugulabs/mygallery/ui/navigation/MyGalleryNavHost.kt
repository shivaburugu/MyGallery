package com.burugulabs.mygallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.burugulabs.mygallery.data.MediaRepository
import com.burugulabs.mygallery.data.ProfileRepository
import com.burugulabs.mygallery.navigation.Route
import com.burugulabs.mygallery.ui.detail.PhotoDetailScreen
import com.burugulabs.mygallery.ui.gallery.GalleryScreen
import com.burugulabs.mygallery.ui.viewmodel.GalleryViewModel
import com.burugulabs.mygallery.ui.viewmodel.PhotoDetailViewModel

@Composable
fun MyGalleryNavHost() {
    val backStack = rememberNavBackStack(Route.Gallery)
    val context = LocalContext.current
    val mediaRepository = remember { MediaRepository(context) }
    val profileRepository = remember { ProfileRepository.getInstance(context) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() }
    ) { key ->
        when (key) {
            is Route.Gallery -> {
                NavEntry(key) {
                    val galleryViewModel: GalleryViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return GalleryViewModel(mediaRepository, profileRepository) as T
                            }
                        }
                    )
                    GalleryScreen(
                        viewModel = galleryViewModel,
                        onImageClick = { mediaItem ->
                            backStack.add(Route.PhotoDetail(mediaItem.uri.toString()))
                        }
                    )
                }
            }
            is Route.PhotoDetail -> {
                NavEntry(key) {
                    val photoDetailViewModel: PhotoDetailViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return PhotoDetailViewModel(profileRepository) as T
                            }
                        }
                    )
                    PhotoDetailScreen(
                        uri = key.uri,
                        viewModel = photoDetailViewModel,
                        onBackClick = { backStack.removeLastOrNull() }
                    )
                }
            }
            else -> error("Unknown route: $key")
        }
    }
}

package com.burugulabs.mygallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burugulabs.mygallery.data.MediaRepository
import com.burugulabs.mygallery.data.model.MediaItem
import kotlinx.coroutines.flow.MutableStateFlow
import com.burugulabs.mygallery.data.ProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val mediaRepository: MediaRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
    val uiState: StateFlow<GalleryUiState> = _uiState.asStateFlow()

    val currentProfileUri: StateFlow<String?> = profileRepository.profilePictureUri
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun loadImages() {
        viewModelScope.launch {
            _uiState.value = GalleryUiState.Loading
            try {
                val images = mediaRepository.fetchImages()
                if (images.isEmpty()) {
                    _uiState.value = GalleryUiState.Empty
                } else {
                    _uiState.value = GalleryUiState.Success(images)
                }
            } catch (e: Exception) {
                _uiState.value = GalleryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed interface GalleryUiState {
    data object Loading : GalleryUiState
    data class Success(val images: List<MediaItem>) : GalleryUiState
    data class Error(val message: String) : GalleryUiState
    data object Empty : GalleryUiState
}

package com.burugulabs.mygallery.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.burugulabs.mygallery.data.ProfileRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PhotoDetailViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val currentProfileUri: StateFlow<String?> = profileRepository.profilePictureUri
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    fun setAsProfilePicture(uri: String) {
        viewModelScope.launch {
            try {
                profileRepository.updateProfilePictureUri(uri)
                _eventFlow.emit(UiEvent.ShowSnackbar("Profile picture updated!"))
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackbar("Failed to update profile picture: ${e.message}"))
            }
        }
    }

    sealed interface UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent
    }
}

package com.burugulabs.mygallery.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {
    @Serializable
    data object Gallery : Route

    @Serializable
    data class PhotoDetail(val uri: String) : Route
}

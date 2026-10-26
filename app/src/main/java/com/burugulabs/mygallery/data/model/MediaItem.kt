package com.burugulabs.mygallery.data.model

import android.net.Uri

data class MediaItem(
    val id: Long,
    val uri: Uri,
    val displayName: String,
    val dateAdded: Long
)

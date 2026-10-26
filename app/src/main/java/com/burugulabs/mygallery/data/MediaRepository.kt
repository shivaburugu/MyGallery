package com.burugulabs.mygallery.data

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import com.burugulabs.mygallery.data.model.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaRepository(private val context: Context) {

    suspend fun fetchImages(): List<MediaItem> = withContext(Dispatchers.IO) {
        val images = mutableListOf<MediaItem>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val date = cursor.getLong(dateColumn)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                images.add(MediaItem(id, contentUri, name, date))
            }
        }
        images
    }
}

package com.burugulabs.mygallery.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "profile_prefs")

class ProfileRepository private constructor(private val context: Context) {

    private object PreferencesKeys {
        val PROFILE_PICTURE_URI = stringPreferencesKey("profile_picture_uri")
    }

    val profilePictureUri: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.PROFILE_PICTURE_URI]
    }

    suspend fun updateProfilePictureUri(uri: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROFILE_PICTURE_URI] = uri
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ProfileRepository? = null

        fun getInstance(context: Context): ProfileRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProfileRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}

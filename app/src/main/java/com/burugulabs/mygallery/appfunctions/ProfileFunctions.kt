package com.burugulabs.mygallery.appfunctions

import androidx.appfunctions.service.AppFunction
import androidx.appfunctions.AppFunctionContext
import com.burugulabs.mygallery.data.ProfileRepository
import kotlinx.coroutines.flow.first

class ProfileFunctions {

    /**
     * Retrieves the user's profile picture URI.
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun getProfilePic(appFunctionContext: AppFunctionContext): String? {
        val repository = ProfileRepository.getInstance(appFunctionContext.context)
        return repository.profilePictureUri.first()
    }
}

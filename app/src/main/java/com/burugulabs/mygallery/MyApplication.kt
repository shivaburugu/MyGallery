package com.burugulabs.mygallery

import android.app.Application
import androidx.appfunctions.service.AppFunctionConfiguration
import com.burugulabs.mygallery.appfunctions.ProfileFunctions

class MyApplication : Application(), AppFunctionConfiguration.Provider {

    override val appFunctionConfiguration: AppFunctionConfiguration
        get() = AppFunctionConfiguration.Builder()
            .addEnclosingClassFactory(ProfileFunctions::class.java) {
                ProfileFunctions()
            }
            .build()
}

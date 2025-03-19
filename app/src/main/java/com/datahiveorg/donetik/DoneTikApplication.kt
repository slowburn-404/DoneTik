package com.datahiveorg.donetik

import android.app.Application
import android.util.Log
import com.datahiveorg.donetik.feature.auth.di.authModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class DoneTikApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        startKoin {
            androidContext(this@DoneTikApplication)
            androidLogger(Level.DEBUG)
            modules(authModule)
        }

    }
}
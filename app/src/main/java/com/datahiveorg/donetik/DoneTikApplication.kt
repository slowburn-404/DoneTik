package com.datahiveorg.donetik

import android.app.Application
import com.datahiveorg.donetik.di.appModule
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Custom Application class for DoneTik.
 *
 * This class is responsible for initializing application-wide components,
 * such as Firebase and Koin dependency injection.
 */
class DoneTikApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this@DoneTikApplication)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@DoneTikApplication)
            modules(appModule)
        }

    }
}
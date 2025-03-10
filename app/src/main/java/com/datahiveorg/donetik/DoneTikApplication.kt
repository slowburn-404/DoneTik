package com.datahiveorg.donetik

import android.app.Application
import com.google.firebase.FirebaseApp

class DoneTikApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)


    }
}
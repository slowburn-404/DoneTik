package com.datahiveorg.donetik.firebase.di

import com.datahiveorg.donetik.firebase.authentication.FirebaseAuthService
import com.datahiveorg.donetik.firebase.authentication.FirebaseAuthServiceImpl
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val firebaseModule = module {

    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    single<FirebaseAuthService> {
        FirebaseAuthServiceImpl(get<FirebaseAuth>())
    }
}
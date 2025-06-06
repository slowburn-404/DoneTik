package com.datahiveorg.donetik.core.firebase.di

import com.datahiveorg.donetik.core.firebase.authentication.AuthDataSource
import com.datahiveorg.donetik.core.firebase.authentication.AuthDataSourceImpl
import com.datahiveorg.donetik.core.firebase.firestore.FireStoreDataSource
import com.datahiveorg.donetik.core.firebase.firestore.FireStoreDataSourceImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.koin.dsl.module

/**
 * Koin module for providing Firebase-related dependencies.
 *
 * This module configures singleton instances of Firebase services like
 * FirebaseAuth and FirebaseFirestore, ensuring that only one instance of each
 * is created and reused throughout the application.
 */
val firebaseModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    single<AuthDataSource> {
        AuthDataSourceImpl(get<FirebaseAuth>())
    }

    single<FirebaseFirestore> {
        Firebase.firestore
    }

    single<FireStoreDataSource> {
        FireStoreDataSourceImpl(
            firestore = get<FirebaseFirestore>()
        )
    }
}
package com.datahiveorg.donetik.core.firebase.di

import com.datahiveorg.donetik.core.firebase.authentication.AuthDataSource
import com.datahiveorg.donetik.core.firebase.authentication.AuthDataSourceImpl
import com.datahiveorg.donetik.core.firebase.firestore.TasksDataSource
import com.datahiveorg.donetik.core.firebase.firestore.TasksDataSourceImpl
import com.datahiveorg.donetik.core.firebase.storage.StorageDataSource
import com.datahiveorg.donetik.core.firebase.storage.StorageDataSourceImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.dsl.module

/**
 * Koin module for providing Firebase-related dependencies.
 *
 * This module configures singleton instances of Firebase services like
 * FirebaseAuth and FirebaseFireStore, ensuring that only one instance of each
 * is created and reused throughout the application.
 */
val firebaseModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    factory<AuthDataSource> {
        AuthDataSourceImpl(get<FirebaseAuth>())
    }

    single<FirebaseFirestore> {
        Firebase.firestore
    }

    factory<TasksDataSource> {
        TasksDataSourceImpl(
            firestore = get<FirebaseFirestore>()
        )
    }

    single<FirebaseStorage> {
        FirebaseStorage.getInstance()
    }

    factory<StorageDataSource> {
        StorageDataSourceImpl(
            storage = get<FirebaseStorage>()
        )
    }
}
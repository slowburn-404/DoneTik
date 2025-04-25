package com.datahiveorg.donetik.firebase.di

import com.datahiveorg.donetik.firebase.authentication.FirebaseAuthService
import com.datahiveorg.donetik.firebase.authentication.FirebaseAuthServiceImpl
import com.datahiveorg.donetik.firebase.firestore.FirebaseFireStoreService
import com.datahiveorg.donetik.firebase.firestore.FirebaseFireStoreServiceImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import org.koin.dsl.module

val firebaseModule = module {
    single<FirebaseAuth> { FirebaseAuth.getInstance() }

    single<FirebaseAuthService> {
        FirebaseAuthServiceImpl(get<FirebaseAuth>())
    }

    single<FirebaseFirestore> {
        Firebase.firestore
    }

    single<FirebaseFireStoreService> {
        FirebaseFireStoreServiceImpl(
            firestore = get<FirebaseFirestore>()
        )
    }
}
package com.datahiveorg.donetik.feature.auth.di

import com.datahiveorg.donetik.feature.auth.data.AuthRepositoryImpl
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.firebase.authentication.FirebaseAuthService
import com.datahiveorg.donetik.util.DispatcherProvider
import com.datahiveorg.donetik.util.dispatcherModule
import org.koin.dsl.module

val authModule = module {
    includes(dispatcherModule)

    single<AuthRepository> {
        AuthRepositoryImpl(
            authService = get<FirebaseAuthService>(),
            dispatcher = get<DispatcherProvider>()
        )
    }

}
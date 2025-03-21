package com.datahiveorg.donetik.feature.auth.di

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.feature.auth.data.AuthRepositoryImpl
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationViewModel
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationNavigator
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationNavigatorImpl
import com.datahiveorg.donetik.firebase.authentication.FirebaseAuthService
import com.datahiveorg.donetik.firebase.di.firebaseModule
import com.datahiveorg.donetik.util.DispatcherProvider
import com.datahiveorg.donetik.util.dispatcherModule
import org.koin.dsl.module

val authModule = module {
    includes(dispatcherModule, firebaseModule)

    single<AuthRepository> {
        AuthRepositoryImpl(
            authService = get<FirebaseAuthService>(),
            dispatcher = get<DispatcherProvider>()
        )
    }

    single<AuthenticationNavigator> {
        AuthenticationNavigatorImpl(
            navController = get<NavHostController>()
        )
    }

    single<AuthenticationViewModel> {
        AuthenticationViewModel(
            authRepository = get<AuthRepository>()
        )
    }

}
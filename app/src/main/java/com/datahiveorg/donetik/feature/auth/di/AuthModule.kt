package com.datahiveorg.donetik.feature.auth.di

import com.datahiveorg.donetik.feature.auth.data.AuthRepositoryImpl
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationViewModel
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationNavigator
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationNavigatorImpl
import com.datahiveorg.donetik.firebase.authentication.AuthDataSource
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.util.DispatcherProvider
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for authentication-related dependencies.
 *
 * This module provides the following dependencies:
 * - [AuthRepository]: Repository for handling authentication-related data operations.
 * - [AuthenticationNavigator]: Navigator for authentication-related screens.
 * - [AuthenticationViewModel]: ViewModel for authentication-related screens.
 */
val authModule = module {

    factory<AuthRepository> {
        AuthRepositoryImpl(
            authDataSource = get<AuthDataSource>(),
            dispatcher = get<DispatcherProvider>(),
        )
    }

    factory<AuthenticationNavigator> { (doneTikNavigator: DoneTikNavigator) ->
        AuthenticationNavigatorImpl(
            doneTikNavigator = doneTikNavigator,
        )
    }

    viewModel<AuthenticationViewModel> {
        AuthenticationViewModel(
            authRepository = get<AuthRepository>()
        )
    }

}
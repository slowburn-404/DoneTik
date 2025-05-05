package com.datahiveorg.donetik.feature.auth.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.datahiveorg.donetik.feature.auth.presentation.screens.AuthenticationScreenWrapper
import com.datahiveorg.donetik.feature.auth.presentation.screens.LoginScreen
import com.datahiveorg.donetik.feature.auth.presentation.screens.SignUpScreen
import com.datahiveorg.donetik.ui.navigatio.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.AuthFeature
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.animatedComposable


fun NavGraphBuilder.authenticationNavGraph(
    navigator: DoneTikNavigator,
    snackBarHostState: SnackbarHostState,
) {
    navigation<AuthFeature>(
        startDestination = AuthenticationScreen.LoginScreen,
    ) {
        animatedComposable<AuthenticationScreen.LoginScreen> {
            AuthenticationScreenWrapper(
                navigator = navigator,
                snackBarHostState = snackBarHostState,
            ) { state, onEvent, onIntent, googleSignHelper ->
                LoginScreen(
                    state = state,
                    onEvent = onEvent,
                    onIntent = onIntent,
                    googleSignHelper = googleSignHelper
                )
            }
        }
        animatedComposable<AuthenticationScreen.SignUpScreen> {
            AuthenticationScreenWrapper(
                navigator = navigator,
                snackBarHostState = snackBarHostState,
            ) { state, onEvent, onIntent, googleSignInHelper ->
                SignUpScreen(
                    state = state,
                    onEvent = onEvent,
                    onIntent = onIntent,
                    googleSignHelper = googleSignInHelper
                )
            }
        }

    }
}
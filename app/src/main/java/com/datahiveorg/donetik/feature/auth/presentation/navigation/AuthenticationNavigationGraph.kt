package com.datahiveorg.donetik.feature.auth.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.datahiveorg.donetik.feature.auth.presentation.screens.AuthenticationScreenWrapper
import com.datahiveorg.donetik.feature.auth.presentation.screens.LoginScreen
import com.datahiveorg.donetik.feature.auth.presentation.screens.SignUpScreen


fun NavGraphBuilder.authenticationNavGraph(
    authenticationNavigator: AuthenticationNavigator,
    route: String,
    snackBarHostState: SnackbarHostState
) {
    navigation(
        startDestination = AuthenticationScreen.LoginScreen.route,
        route = route
    ) {

        composable(AuthenticationScreen.LoginScreen.route) {

            AuthenticationScreenWrapper(
                navigator = authenticationNavigator,
                snackBarHostState = snackBarHostState,
            ) { state, onEvent, onIntent ->
                LoginScreen(
                    state = state,
                    onEvent = onEvent,
                    onIntent = onIntent
                )
            }
        }

        composable(AuthenticationScreen.SignUpScreen.route) {
            AuthenticationScreenWrapper(
                navigator = authenticationNavigator,
                snackBarHostState = snackBarHostState
            ) { state, onEvent, onIntent ->
                SignUpScreen(
                    state = state,
                    onEvent = onEvent,
                    onIntent = onIntent
                )
            }
        }

    }
}
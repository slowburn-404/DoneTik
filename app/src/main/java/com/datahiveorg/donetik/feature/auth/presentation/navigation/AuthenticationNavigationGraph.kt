package com.datahiveorg.donetik.feature.auth.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.datahiveorg.donetik.feature.auth.presentation.screens.AuthenticationScreenWrapper
import com.datahiveorg.donetik.feature.auth.presentation.screens.LoginScreen
import com.datahiveorg.donetik.router.RouterScreen
import com.datahiveorg.donetik.feature.auth.presentation.screens.SignUpScreen
import com.datahiveorg.donetik.ui.navigation.NavigatorFactory


fun NavGraphBuilder.authenticationNavGraph(
    navigatorFactory: NavigatorFactory,
    route: String,
    snackBarHostState: SnackbarHostState,
) {
    navigation(
        startDestination = AuthenticationScreen.LoginScreen.route,
        route = route
    ) {
        val authenticationNavigator = navigatorFactory.create<AuthenticationNavigator>()

        composable(AuthenticationScreen.LoginScreen.route) {
            AuthenticationScreenWrapper(
                navigator = authenticationNavigator,
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

        composable(AuthenticationScreen.SignUpScreen.route) {
            AuthenticationScreenWrapper(
                navigator = authenticationNavigator,
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
package com.datahiveorg.donetik.feature.auth.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.datahiveorg.donetik.feature.auth.presentation.screens.AuthenticationScreenWrapper
import com.datahiveorg.donetik.feature.auth.presentation.screens.LoginScreen
import com.datahiveorg.donetik.feature.auth.presentation.screens.SignUpScreen
import com.datahiveorg.donetik.core.ui.navigation.AuthFeature
import com.datahiveorg.donetik.core.ui.navigation.animatedComposable
import com.datahiveorg.donetik.util.GoogleSignHelper


/**
 * Defines the navigation graph for the authentication feature.
 *
 * This function sets up the navigation flow for the login and sign-up screens
 * within the authentication module. It utilizes a nested navigation graph
 * specific to the `AuthFeature`.
 *
 * Each screen (`LoginScreen`, `SignUpScreen`) is wrapped with `AuthenticationScreenWrapper`
 * which likely provides common UI elements or logic for authentication screens.
 *
 * @param authenticationNavigator An instance of [AuthenticationNavigator] responsible for handling navigation actions within the authentication flow.
 * @param snackBarHostState The [SnackbarHostState] used to display snack bars (e.g., for error messages or notifications) within the authentication screens.
 */
fun NavGraphBuilder.authenticationNavGraph(
    authenticationNavigator: AuthenticationNavigator,
    snackBarHostState: SnackbarHostState,
    googleSignHelper: GoogleSignHelper
) {

    navigation<AuthFeature>(
        startDestination = LoginScreen,
    ) {
        animatedComposable<LoginScreen> {
            AuthenticationScreenWrapper(
                navigator = authenticationNavigator,
                snackBarHostState = snackBarHostState,
                googleSignInHelper = googleSignHelper,
                content = { state, onEvent, onIntent, googleSignHelper ->
                    LoginScreen(
                        state = state,
                        onEvent = onEvent,
                        onIntent = onIntent,
                        googleSignHelper = googleSignHelper
                    )
                }
            )
        }
        animatedComposable<SignUpScreen> {
            AuthenticationScreenWrapper(
                navigator = authenticationNavigator,
                snackBarHostState = snackBarHostState,
                googleSignInHelper = googleSignHelper,
                content = { state, onEvent, onIntent, googleSignInHelper ->
                    SignUpScreen(
                        state = state,
                        onEvent = onEvent,
                        onIntent = onIntent,
                        googleSignHelper = googleSignInHelper
                    )
                }
            )
        }

    }
}
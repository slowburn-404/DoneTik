package com.datahiveorg.donetik.feature.auth.presentation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationIntent
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationUiEvent
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationUiState
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationViewModel
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationNavigator
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthenticationScreenWrapper(
    viewModel: AuthenticationViewModel = koinViewModel(),
    navigator: AuthenticationNavigator,
    snackBarHostState: SnackbarHostState,
    content: @Composable (
        state: AuthenticationUiState,
        onEvent: (AuthenticationUiEvent) -> Unit,
        onIntent: (AuthenticationIntent) -> Unit,
    ) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val uiEvent by viewModel.uiEvents.collectAsStateWithLifecycle(initialValue = AuthenticationUiEvent.Idle)

    LaunchedEffect(key1 = true) {
        when (uiEvent) {
            is AuthenticationUiEvent.Idle -> {}
            is AuthenticationUiEvent.ShowSnackBar -> {
                snackBarHostState
                    .showSnackbar((uiEvent as AuthenticationUiEvent.ShowSnackBar).message)
            }

            is AuthenticationUiEvent.Navigate.Login -> {
                navigator.navigate(AuthenticationScreen.LoginScreen)
            }

            is AuthenticationUiEvent.Navigate.SignUp -> {
                navigator.navigate(AuthenticationScreen.SignUpScreen)
            }
        }
    }

    content(
        state,
        viewModel::emitEvent,
        viewModel::emitIntent,
    )

}
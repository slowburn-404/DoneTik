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
import com.datahiveorg.donetik.util.GoogleSignHelper
import kotlinx.coroutines.flow.collectLatest
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
        googleSignInHelper: GoogleSignHelper
    ) -> Unit,
    googleSignInHelper: GoogleSignHelper
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is AuthenticationUiEvent.ShowSnackBar -> snackBarHostState
                    .showSnackbar(event.message)

                is AuthenticationUiEvent.Navigate.Login -> navigator.navigateToLogin()

                is AuthenticationUiEvent.Navigate.SignUp -> navigator.navigateToSignUp()

                is AuthenticationUiEvent.Navigate.Home -> navigator.navigateToHomeFeature()

                is AuthenticationUiEvent.Navigate.UpdateUsername -> navigator.navigateToUpdateUsername()
            }
        }
    }

    content(
        state,
        viewModel::emitEvent,
        viewModel::emitIntent,
        googleSignInHelper
    )

}
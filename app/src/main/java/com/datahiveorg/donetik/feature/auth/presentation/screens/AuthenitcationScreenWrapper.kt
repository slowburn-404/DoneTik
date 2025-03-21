package com.datahiveorg.donetik.feature.auth.presentation.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationNavigator
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationScreen

@Composable
fun AuthenticationScreenWrapper(
    navigator: AuthenticationNavigator,
    snackBarHostState: SnackbarHostState,
    content: @Composable (
        onEvent: () -> Unit,
        onNavigate: (AuthenticationScreen) -> Unit
    ) -> Unit
) {

    content(
        {},
        {},
    )


}
package com.datahiveorg.donetik.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.datahiveorg.donetik.feature.auth.presentation.navigation.authenticationNavGraph
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    snackBarHostState: SnackbarHostState,
    navHostController: NavHostController,
) {
    val navigatorFactory = getKoin().get<NavigatorFactory> { parametersOf(navHostController) }

    NavHost(
        modifier = modifier
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
        navController = navHostController,
        startDestination = "auth"
    ) {
        authenticationNavGraph(
            navigatorFactory = navigatorFactory,
            route = "auth",
            snackBarHostState = snackBarHostState,
        )
    }
}
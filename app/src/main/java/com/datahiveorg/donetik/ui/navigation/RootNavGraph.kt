package com.datahiveorg.donetik.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationNavigator
import com.datahiveorg.donetik.feature.auth.presentation.navigation.authenticationNavGraph
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.feature.home.presentation.navigation.homeNavigationGraph
import com.datahiveorg.donetik.feature.onboarding.presentation.OnBoardingEvents
import com.datahiveorg.donetik.feature.onboarding.presentation.OnBoardingScreen
import com.datahiveorg.donetik.feature.onboarding.presentation.OnBoardingViewModel
import com.datahiveorg.donetik.feature.router.RouterScreen
import com.datahiveorg.donetik.feature.router.RouterViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    snackBarHostState: SnackbarHostState,
    navigator: DoneTikNavigator,
    navController: NavHostController,
) {
    val authNavigator = getKoin().get<AuthenticationNavigator> { parametersOf(navigator) }
    val homeNavigator = getKoin().get<HomeNavigator> { parametersOf(navigator) }

    NavHost(
        modifier = modifier
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
        navController = navController,
        startDestination = RouterScreen
    ) {


        authenticationNavGraph(
            authenticationNavigator = authNavigator,
            snackBarHostState = snackBarHostState,
        )

        homeNavigationGraph(
            homeNavigator = homeNavigator,
            snackBarHostState = snackBarHostState,
        )

        animatedComposable<RouterScreen> {
            RouterScreen(
                viewModel = koinViewModel<RouterViewModel>(),
                onNavigate = { screen ->
                    navigator.navigate(screen)
                }
            )
        }

        animatedComposable<OnBoardingFeature> {
            OnBoardingScreen(
                doneTikNavigator = navigator,
                viewModel = koinViewModel<OnBoardingViewModel>()
            )
        }
    }
}
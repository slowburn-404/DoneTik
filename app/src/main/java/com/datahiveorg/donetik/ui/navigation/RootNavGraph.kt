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
import com.datahiveorg.donetik.feature.auth.presentation.navigation.authenticationNavGraph
import com.datahiveorg.donetik.feature.onboarding.presentation.OnBoardingEvents
import com.datahiveorg.donetik.feature.onboarding.presentation.OnBoardingScreen
import com.datahiveorg.donetik.feature.onboarding.presentation.OnBoardingViewModel
import com.datahiveorg.donetik.router.RouterEvent
import com.datahiveorg.donetik.router.RouterScreen
import com.datahiveorg.donetik.router.RouterViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

@Composable
fun RootNavGraph(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    snackBarHostState: SnackbarHostState,
    navHostController: NavHostController,
    routerViewModel: RouterViewModel = koinViewModel(),
    onBoardingViewModel: OnBoardingViewModel = koinViewModel()
) {
    val navigatorFactory = getKoin().get<NavigatorFactory> { parametersOf(navHostController) }

    NavHost(
        modifier = modifier
            .padding(paddingValues)
            .padding(horizontal = 20.dp),
        navController = navHostController,
        startDestination = RouterScreen.route
    ) {
        authenticationNavGraph(
            navigatorFactory = navigatorFactory,
            route = AuthFeature.route,
            snackBarHostState = snackBarHostState,
        )

        animatedComposable(RouterScreen.route) {
            val event by routerViewModel.event.collectAsStateWithLifecycle(initialValue = RouterEvent.None)
            val state by routerViewModel.state.collectAsStateWithLifecycle()

            RouterScreen(
                event = event,
                onNavigate = { screen ->
                    navHostController.navigate(screen.route) {
                        launchSingleTop = true
                        popUpTo(RouterScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        animatedComposable(OnBoardingFeature.route) {
            val event by onBoardingViewModel.events.collectAsStateWithLifecycle(initialValue = OnBoardingEvents.None)
            val state by onBoardingViewModel.state.collectAsStateWithLifecycle()

            OnBoardingScreen(
                onNavigate = { screen ->
                    navHostController.navigate(screen.route) {
                        launchSingleTop = true
                        popUpTo(OnBoardingFeature.route) {
                            inclusive = true
                        }
                    }
                },
                onEvent = onBoardingViewModel::emitEvent,
                state = state
            )
        }
    }
}
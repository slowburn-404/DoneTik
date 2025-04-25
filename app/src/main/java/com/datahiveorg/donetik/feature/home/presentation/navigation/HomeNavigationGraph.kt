package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.datahiveorg.donetik.feature.home.presentation.feed.FeedScreen
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.HomeFeature
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.homeNavigationGraph(
    navigator: DoneTikNavigator,
    snackBarHostState: SnackbarHostState,
    ) {

    navigation(
        startDestination = HomeScreen.Feed.route,
        route = HomeFeature.route
    ) {
        composable(HomeScreen.Feed.route) {
            FeedScreen(
                navigator = navigator,
                snackBarHostState = snackBarHostState
            )
        }


    }
}
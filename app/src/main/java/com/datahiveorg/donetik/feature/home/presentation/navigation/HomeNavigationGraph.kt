package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.datahiveorg.donetik.feature.home.presentation.feed.FeedScreen
import com.datahiveorg.donetik.feature.home.presentation.newtask.NewTaskScreen
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.HomeFeature
import com.datahiveorg.donetik.ui.navigation.animatedComposable

fun NavGraphBuilder.homeNavigationGraph(
    navigator: DoneTikNavigator,
    snackBarHostState: SnackbarHostState,
) {
    navigation(
        startDestination = HomeScreen.Feed.route,
        route = HomeFeature.route
    ) {
        animatedComposable(HomeScreen.Feed.route) {
            FeedScreen(
                navigator = navigator,
                snackBarHostState = snackBarHostState
            )
        }

        animatedComposable(HomeScreen.NewTaskScreen.route) {
            NewTaskScreen(
                navigator = navigator,
                snackBarHostState = snackBarHostState
            )
        }
    }
}
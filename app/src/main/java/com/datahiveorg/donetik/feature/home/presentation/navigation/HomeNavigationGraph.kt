package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.datahiveorg.donetik.feature.home.presentation.feed.FeedScreen
import com.datahiveorg.donetik.feature.home.presentation.newtask.NewTaskScreen
import com.datahiveorg.donetik.feature.home.presentation.taskview.TaskViewScreen
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.HomeFeature
import com.datahiveorg.donetik.ui.navigation.animatedComposable

fun NavGraphBuilder.homeNavigationGraph(
    navigator: DoneTikNavigator,
    snackBarHostState: SnackbarHostState,
) {
    navigation<HomeFeature>(
        startDestination = HomeScreen.Feed,
    ) {
        animatedComposable<HomeScreen.Feed> {
            FeedScreen(
                navigator = navigator,
                snackBarHostState = snackBarHostState
            )
        }

        animatedComposable<HomeScreen.NewTaskScreen> {
            NewTaskScreen(
                navigator = navigator,
                snackBarHostState = snackBarHostState
            )
        }

        animatedComposable<HomeScreen.TaskScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<HomeScreen.TaskScreen>()

            TaskViewScreen(
                taskId = route.taskId,
                userId = route.userId,
                navigator = navigator,
                snackBarHostState = snackBarHostState
            )
        }
    }
}
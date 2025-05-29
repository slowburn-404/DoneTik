package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.datahiveorg.donetik.feature.home.presentation.feed.FeedScreen
import com.datahiveorg.donetik.feature.home.presentation.feed.FeedViewModel
import com.datahiveorg.donetik.feature.home.presentation.newtask.NewTaskScreen
import com.datahiveorg.donetik.feature.home.presentation.newtask.NewTaskViewModel
import com.datahiveorg.donetik.feature.home.presentation.taskview.TaskViewModel
import com.datahiveorg.donetik.feature.home.presentation.taskview.TaskViewScreen
import com.datahiveorg.donetik.ui.navigation.HomeFeature
import com.datahiveorg.donetik.ui.navigation.animatedComposable
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.homeNavigationGraph(
    homeNavigator: HomeNavigator,
    snackBarHostState: SnackbarHostState,
) {

    navigation<HomeFeature>(
        startDestination = Feed,
    ) {
        composable<Feed> {
            FeedScreen(
                viewModel = koinViewModel<FeedViewModel>(),
                navigator = homeNavigator,
                snackBarHostState = snackBarHostState
            )
        }
        animatedComposable<NewTaskScreen> {
            NewTaskScreen(
                viewModel = koinViewModel<NewTaskViewModel>(),
                navigator = homeNavigator,
                snackBarHostState = snackBarHostState
            )
        }

        animatedComposable<TaskScreen> { backStackEntry ->
            val route = backStackEntry.toRoute<TaskScreen>()

            TaskViewScreen(
                viewModel = koinViewModel<TaskViewModel>(),
                taskId = route.taskId,
                userId = route.userId,
                navigator = homeNavigator,
                snackBarHostState = snackBarHostState
            )
        }
    }
}
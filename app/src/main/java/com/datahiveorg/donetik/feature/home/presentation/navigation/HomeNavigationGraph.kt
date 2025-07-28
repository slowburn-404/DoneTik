package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.datahiveorg.donetik.feature.home.presentation.feed.FeedScreen
import com.datahiveorg.donetik.feature.home.presentation.feed.FeedViewModel
import com.datahiveorg.donetik.feature.home.presentation.newtask.NewTaskScreen
import com.datahiveorg.donetik.feature.home.presentation.newtask.NewTaskViewModel
import com.datahiveorg.donetik.feature.home.presentation.taskview.TaskViewModel
import com.datahiveorg.donetik.feature.home.presentation.taskview.TaskViewScreen
import com.datahiveorg.donetik.core.ui.navigation.HomeFeature
import com.datahiveorg.donetik.core.ui.navigation.animatedComposable
import com.datahiveorg.donetik.feature.home.presentation.tasklist.TaskListScreen
import com.datahiveorg.donetik.feature.home.presentation.tasklist.TaskListViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Defines the navigation graph for the home feature.
 *
 * This function sets up the routes and destinations within the home feature.
 *
 * @param homeNavigator The navigator for handling navigation events within the home feature.
 * @param snackBarHostState The [SnackbarHostState] for displaying snack bars.
 */
fun NavGraphBuilder.homeNavigationGraph(
    homeNavigator: HomeNavigator,
    snackBarHostState: SnackbarHostState,
) {

    navigation<HomeFeature>(
        startDestination = Feed,
    ) {
        animatedComposable<Feed> {
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
            val navArgs = backStackEntry.toRoute<TaskScreen>()

            TaskViewScreen(
                viewModel = koinViewModel<TaskViewModel>(),
                taskId = navArgs.taskId,
                userId = navArgs.userId,
                navigator = homeNavigator,
                snackBarHostState = snackBarHostState
            )
        }

        animatedComposable<TaskListScreen> { backStackEntry ->
            val navArgs = backStackEntry.toRoute<TaskListScreen>()

            TaskListScreen(
                viewModel = koinViewModel<TaskListViewModel>(),
                navigator = homeNavigator,
                snackBar = snackBarHostState,
                category = navArgs.category,
                filterOption = navArgs.filterOption
            )

        }
    }
}
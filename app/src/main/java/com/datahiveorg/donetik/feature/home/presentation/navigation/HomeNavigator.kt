package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.FeatureScreen

internal class HomeNavigatorImpl(
    private val navController: NavHostController
) : DoneTikNavigator {
    override fun navigate(destination: FeatureScreen) {
        when (destination) {
            is HomeScreen.Feed -> navigateToFeedScreen()
            is HomeScreen.NewTaskScreen -> navigateToNewTaskScreen()
            is HomeScreen.TaskScreen -> navigateToTaskViewScreen(
                taskId = destination.taskId,
                userId = destination.userId
            )
        }
    }

    private fun navigateToFeedScreen() {
        navController.navigate(HomeScreen.Feed) {
            launchSingleTop = true
            popUpTo<HomeScreen.Feed> {
                inclusive = true
            }
        }
    }

    private fun navigateToNewTaskScreen() {
        navController.navigate(HomeScreen.NewTaskScreen) {
            launchSingleTop = true
            popUpTo<HomeScreen.Feed> {
                inclusive = true
            }
        }
    }

    private fun navigateToTaskViewScreen(taskId: String, userId: String) {
        navController.navigate(HomeScreen.TaskScreen(taskId = taskId, userId = userId)) {
            launchSingleTop = true
            popUpTo<HomeScreen.Feed> {
                inclusive = true
            }
        }

    }

}
package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.FeatureScreen

internal class HomeNavigatorImpl(
    private val navController: NavHostController
) : DoneTikNavigator {
    override fun navigate(screen: FeatureScreen) {
        when (screen) {
            is HomeScreen.Feed -> navigateToFeedScreen()
            is HomeScreen.NewTaskScreen -> navigateToNewTaskScreen()
            is HomeScreen.TaskScreen -> navigateToTaskScreen(
                taskId = screen.taskId,
                userId = screen.userId
            )
        }
    }

    private fun navigateToFeedScreen() {
        navController.navigate(HomeScreen.Feed.route) {
            launchSingleTop = true
            popUpTo<HomeScreen.Feed> {
                inclusive = true
            }
        }
    }

    private fun navigateToNewTaskScreen() {
        navController.navigate(HomeScreen.NewTaskScreen.route) {
            launchSingleTop = true
            popUpTo<HomeScreen.Feed> {
                inclusive = true
            }
        }

    }

    private fun navigateToTaskScreen(taskId: String, userId: String) {
        navController.navigate(HomeScreen.TaskScreen(taskId = taskId, userId = userId).route) {
            launchSingleTop = true
            popUpTo(HomeScreen.Feed.route) {
                inclusive = true
            }
        }

    }

}
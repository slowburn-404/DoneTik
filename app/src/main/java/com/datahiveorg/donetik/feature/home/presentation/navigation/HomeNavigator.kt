package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.FeatureScreen

internal class HomeNavigatorImpl(
    private val navController: NavHostController
) : DoneTikNavigator {
    override fun navigate(screen: FeatureScreen) {
        when (screen) {
            is HomeScreen.MainScreen -> navigateToMainScreen(screen.userId)
            is HomeScreen.NewTaskScreen -> navigateToNewTaskScreen()
            is HomeScreen.TaskScreen -> navigateToTaskScreen(screen.taskId)
        }
    }

    private fun navigateToMainScreen(userId: String) {
        navController.navigate(HomeScreen.MainScreen(userId = userId).route) {
            launchSingleTop = true
            popUpTo<HomeScreen.MainScreen> {
                inclusive = true
            }
        }
    }

    private fun navigateToNewTaskScreen() {
        navController.navigate(HomeScreen.NewTaskScreen.route) {
            launchSingleTop = true
            popUpTo<HomeScreen.MainScreen> {
                inclusive = true
            }
        }

    }

    private fun navigateToTaskScreen(taskId: String) {
        navController.navigate(HomeScreen.TaskScreen(taskId).route) {
            launchSingleTop = true
            popUpTo(HomeScreen.MainScreen(userId = "").route) {
                inclusive = true
            }
        }

    }

}
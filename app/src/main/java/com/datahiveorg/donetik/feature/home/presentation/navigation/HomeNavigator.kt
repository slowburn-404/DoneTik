package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.navigation.NavHostController

interface HomeNavigator {
    fun navigate(screen: HomeScreen)
}

internal class HomeNavigatorImpl(
    private val navController: NavHostController
) : HomeNavigator {
    override fun navigate(screen: HomeScreen) {
        when (screen) {
            is HomeScreen.MainScreen -> navigateToMainScreen(screen.userId)
            is HomeScreen.NewTaskScreen -> navigateToNewTaskScreen()
            is HomeScreen.TaskScreen -> navigateToTaskScreen(screen.taskId)
        }
    }

    private fun navigateToMainScreen(userId: String) {
        navController.navigate(HomeScreen.MainScreen(userId = userId)) {
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
        navController.navigate(HomeScreen.TaskScreen(taskId)) {
            launchSingleTop = true
            popUpTo<HomeScreen.MainScreen> {
                inclusive = true
            }
        }

    }

}
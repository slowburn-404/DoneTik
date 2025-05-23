package com.datahiveorg.donetik.feature.home.presentation.navigation

import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator

interface HomeNavigator {
    fun navigateToFeedScreen()

    fun navigateToNewTaskScreen()

    fun navigateToTaskViewScreen(taskId: String, userId: String)
}

class HomeNavigatorImpl(
    private val doneTikNavigator: DoneTikNavigator
) : HomeNavigator {

    override fun navigateToFeedScreen() {
        doneTikNavigator.navigate(
            destination = HomeScreen.Feed
        )
    }

    override fun navigateToNewTaskScreen() {
        doneTikNavigator.navigate(
            destination = HomeScreen.NewTaskScreen
        )
    }

    override fun navigateToTaskViewScreen(taskId: String, userId: String) {
        doneTikNavigator.navigate(
            destination = HomeScreen.TaskScreen(taskId = taskId, userId = userId)
        )
    }
}
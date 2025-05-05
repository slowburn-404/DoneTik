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
            destination = HomeScreen.Feed,
            navOptions = HomeScreen.Feed.toNavOptions()
        )
    }

    override fun navigateToNewTaskScreen() {
        doneTikNavigator.navigate(
            destination = HomeScreen.NewTaskScreen,
            navOptions = HomeScreen.NewTaskScreen.toNavOptions()
        )
    }

    override fun navigateToTaskViewScreen(taskId: String, userId: String) {
        doneTikNavigator.navigate(
            destination = HomeScreen.TaskScreen(taskId = taskId, userId = userId),
            navOptions = HomeScreen.TaskScreen(taskId = taskId, userId = userId).toNavOptions()
        )
    }
}
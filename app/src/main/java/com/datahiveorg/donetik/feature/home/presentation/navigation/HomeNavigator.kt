package com.datahiveorg.donetik.feature.home.presentation.navigation

import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator

interface HomeNavigator {
    fun navigateToFeedScreen()

    fun navigateToNewTaskScreen()

    fun navigateToTaskViewScreen(taskId: String, userId: String)

    fun navigateToTaskListScreen()

    fun navigateUp()
}

class HomeNavigatorImpl(
    private val doneTikNavigator: DoneTikNavigator
) : HomeNavigator {

    override fun navigateToFeedScreen() {
        doneTikNavigator.navigate(
            destination = Feed
        )
    }

    override fun navigateToNewTaskScreen() {
        doneTikNavigator.navigate(
            destination = NewTaskScreen
        )
    }

    override fun navigateToTaskViewScreen(taskId: String, userId: String) {
        doneTikNavigator.navigate(
            destination = TaskScreen(taskId = taskId, userId = userId)
        )
    }

    override fun navigateUp() {
        doneTikNavigator.navigateUp()
    }

    override fun navigateToTaskListScreen() {
       doneTikNavigator.navigate(
           destination = TaskListScreen
       )
    }
}
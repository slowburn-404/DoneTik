package com.datahiveorg.donetik.feature.home.presentation.navigation

import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class HomeScreen(
    override val route: String,
    override val title: String
) : FeatureScreen {
    @Serializable
    data class MainScreen(val userId: String) : HomeScreen(
        route = "main_screen",
        title = "Home"
    )

    @Serializable
    data object NewTaskScreen : HomeScreen(
        route = "new_task",
        title = "New Task"
    )

    @Serializable
    data class TaskScreen(val taskId: String) : HomeScreen(
        route = "task_screen",
        title = "Task"
    )
}
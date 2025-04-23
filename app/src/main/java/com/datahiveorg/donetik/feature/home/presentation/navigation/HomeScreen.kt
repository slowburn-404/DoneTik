package com.datahiveorg.donetik.feature.home.presentation.navigation

import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class HomeScreen : FeatureScreen {
    @Serializable
    data class Feed(val userId: String) : HomeScreen() {
        override val route: String
            get() = "main_screen"
        override val title: String
            get() = "Home"
        override val hasBottomBar: Boolean
            get() = true
    }

    @Serializable
    data object NewTaskScreen : HomeScreen() {
        override val route: String
            get() = "new_task_screen"
        override val title: String
            get() = "New Task"
        override val hasBottomBar: Boolean
            get() = true
        override val hasBackButton: Boolean
            get() = true
        override val hasTopAppBar: Boolean
            get() = true
    }

    @Serializable
    data class TaskScreen(val taskId: String, val userId: String) : HomeScreen() {
        override val route: String
            get() = "task_screen"
        override val title: String
            get() = "Task"
        override val hasTopAppBar: Boolean
            get() = true
        override val hasBottomBar: Boolean
            get() = true
        override val hasBackButton: Boolean
            get() = true
    }
}
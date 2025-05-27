package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.ui.navigation.RouterScreen
import com.datahiveorg.donetik.ui.navigation.ScreenUIConfig
import kotlinx.serialization.Serializable

sealed class HomeScreen : FeatureScreen {
    @Serializable
    data object Feed : HomeScreen() {
        override fun buildNavOptions(builder: NavOptionsBuilder) {
            builder.popUpTo(RouterScreen) {
                inclusive = false
                saveState = true
            }
            builder.restoreState = true
        }

        override val screenUIConfig: ScreenUIConfig
            get() = ScreenUIConfig(
                hasTopAppBar = true,
                hasBottomBar = true,
                hasFAB = true,
                bottomNavIconRes = R.drawable.ic_home,
                enterTransition = homeEnterTransition(),
                exitTransition = homeExitTransition()
            )
    }

    @Serializable
    data object NewTaskScreen : HomeScreen() {
        override fun buildNavOptions(builder: NavOptionsBuilder) {
            builder.popUpTo(Feed) {
                inclusive = false
                saveState = true
            }
            builder.restoreState = true
        }

        override val screenUIConfig: ScreenUIConfig
            get() = ScreenUIConfig(
                title = "New Task",
                hasTopAppBar = true,
                hasBottomBar = false,
                hasFAB = false,
                hasNavIcon = true,
                navIconRes = R.drawable.ic_arrow_back,
                enterTransition = homeEnterTransition(),
                exitTransition = homeExitTransition()
            )
    }

    @Serializable
    data class TaskScreen(val taskId: String, val userId: String) : HomeScreen() {
        override fun buildNavOptions(builder: NavOptionsBuilder) {
            builder.popUpTo<TaskScreen> {
                inclusive = true
            }
        }

        override val screenUIConfig: ScreenUIConfig
            get() = ScreenUIConfig(
                hasTopAppBar = true,
                hasBottomBar = false,
                hasFAB = false,
                hasNavIcon = true,
                navIconRes = R.drawable.ic_arrow_back,
                enterTransition = homeEnterTransition(),
                exitTransition = homeExitTransition()
            )
    }
}
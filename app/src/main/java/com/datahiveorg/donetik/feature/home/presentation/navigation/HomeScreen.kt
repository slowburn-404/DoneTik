package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.core.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.core.ui.navigation.HomeFeature
import com.datahiveorg.donetik.core.ui.navigation.ScreenUIConfig
import com.datahiveorg.donetik.feature.home.presentation.tasklist.FilterOption
import kotlinx.serialization.Serializable

@Serializable
data object Feed : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<HomeFeature> {
            inclusive = false
            saveState = true
        }
        builder.restoreState = true
    }

    override val screenUIConfig: ScreenUIConfig
        get() = ScreenUIConfig(
            title = "",
            hasTopAppBar = true,
            hasBottomBar = true,
            hasFAB = true,
            bottomNavIconRes = R.drawable.ic_home,
            enterTransition = homeEnterTransition(),
            exitTransition = homeExitTransition(),
        )
}

@Serializable
data object NewTaskScreen : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<Feed> {
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
data class TaskScreen(val taskId: String, val userId: String) : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<Feed> {
            inclusive = false
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

@Serializable
data class TaskListScreen(val category: String, val filterOption: FilterOption) : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<Feed> {
            inclusive = false
            saveState = true
        }
        builder.restoreState = true
    }

    override val screenUIConfig: ScreenUIConfig
        get() = ScreenUIConfig(
            title = "Pending Tasks",
            hasTopAppBar = false,
            hasBottomBar = false,
            hasFAB = true,
            hasNavIcon = true,
            navIconRes = R.drawable.ic_arrow_back,
            enterTransition = homeEnterTransition(),
            exitTransition = homeExitTransition()
        )
}
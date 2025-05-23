package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.ui.navigation.HomeFeature
import com.datahiveorg.donetik.ui.navigation.RouterScreen
import kotlinx.serialization.Serializable

sealed class HomeScreen : FeatureScreen {
    @Serializable
    data object Feed : HomeScreen() {
        override fun buildNavOptions(builder: NavOptionsBuilder) {
            builder.popUpTo<RouterScreen> {
                inclusive = false
                saveState = true
            }
            builder.restoreState = true
        }

        override val hasBottomBar: Boolean
            get() = true
        override val hasFAB: Boolean
            get() = true
        override val bottomNavIconRes: Int
            get() = R.drawable.ic_home
        override val hasTopAppBar: Boolean
            get() = true
    }

    @Serializable
    data object NewTaskScreen : HomeScreen() {
        override fun buildNavOptions(builder: NavOptionsBuilder) {
            builder.popUpTo<Feed> {
                inclusive = false
                saveState = true
            }
            builder.restoreState = true
        }
        override val title: String
            get() = "New Task"
        override val hasNavIcon: Boolean
            get() = true
        override val hasTopAppBar: Boolean
            get() = true
        override val navIconRes: Int
            get() = R.drawable.ic_arrow_back
    }

    @Serializable
    data class TaskScreen(val taskId: String, val userId: String) : HomeScreen() {
        override fun buildNavOptions(builder: NavOptionsBuilder) {
            builder.popUpTo<Feed> {
                inclusive = false
            }
        }
        override val hasTopAppBar: Boolean
            get() = true
        override val hasNavIcon: Boolean
            get() = true
        override val navIconRes: Int
            get() = R.drawable.ic_arrow_back

    }
}
package com.datahiveorg.donetik.feature.home.presentation.navigation

import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.serialization.Serializable

sealed class HomeScreen : FeatureScreen {
    @Serializable
    data object Feed : HomeScreen() {
        override val title: String
            get() = "Feed"
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
        override val title: String
            get() = ""
        override val hasTopAppBar: Boolean
            get() = true
        override val hasNavIcon: Boolean
            get() = true
        override val navIconRes: Int
            get() = R.drawable.ic_arrow_back

    }
}
package com.datahiveorg.donetik.ui.navigation

import androidx.annotation.DrawableRes
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen

interface FeatureScreen {
    val route: String
    val title: String

    //have default values and override only when necessary
    val hasTopAppBar: Boolean get() = false
    val hasBottomBar: Boolean get() = false
    val hasNavIcon: Boolean get() = false
    val topBarActions: List<TopBarAction> get() = emptyList()
    val hasFAB: Boolean get() = false

    @get:DrawableRes
    val bottomNavIconRes: Int? get() = null

    @get:DrawableRes
    val navIconRes: Int? get() = null
}

data class TopBarAction(
    @DrawableRes val iconRes: Int,
    val description: String,
    val onClick: () -> Unit
)

data object RouterScreen : FeatureScreen {
    override val route: String
        get() = "router_screen"

    override val title: String
        get() = ""
}

data object AuthFeature : FeatureScreen {
    override val title: String
        get() = ""

    override val route: String
        get() = "auth_feature"
}

data object OnBoardingFeature : FeatureScreen {
    override val title: String
        get() = ""

    override val route: String
        get() = "onboarding_feature"
}

data object HomeFeature : FeatureScreen {
    override val route: String
        get() = "home_feature"
    override val title: String
        get() = "Home"
}

fun FeatureScreen.getFABDestination(): String {
    return when (this) {
        is HomeScreen.Feed -> HomeScreen.NewTaskScreen.route
        else -> ""
    }
}
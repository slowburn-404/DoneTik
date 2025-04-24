package com.datahiveorg.donetik.ui.navigation

import androidx.annotation.DrawableRes
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen

interface FeatureScreen {
    val route: String
    val title: String

    //have default values and override only when needed
    val hasTopAppBar: Boolean get() = false
    val hasBottomBar: Boolean get() = false
    val hasBackButton: Boolean get() = false
    val topBarActions: List<TopBarAction> get() = emptyList()
    val hasFAB: Boolean get() = false

    @get:DrawableRes
    val iconRes: Int? get() = null
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
        get() = "auth"
}

data object OnBoardingFeature : FeatureScreen {
    override val title: String
        get() = ""

    override val route: String
        get() = "onboarding"
}

data object HomeFeature : FeatureScreen {
    override val route: String
        get() = "home"
    override val title: String
        get() = "Home"
}
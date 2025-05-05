package com.datahiveorg.donetik.ui.navigation

import androidx.annotation.DrawableRes
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen
import kotlinx.serialization.Serializable

interface FeatureScreen {
    val title: String get() = ""

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

@Serializable
data object RouterScreen : FeatureScreen

@Serializable
data object AuthFeature : FeatureScreen

@Serializable
data object OnBoardingFeature : FeatureScreen


@Serializable
data object HomeFeature : FeatureScreen

fun FeatureScreen.getFABDestination(): FeatureScreen {
    return when (this) {
        is HomeScreen.Feed -> HomeScreen.NewTaskScreen
        else -> this//trash code TODO(Find a better way when adding more screens)
    }
}
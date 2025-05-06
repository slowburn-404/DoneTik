package com.datahiveorg.donetik.feature.home.presentation.navigation

import com.datahiveorg.donetik.ui.navigation.NavOptions
import com.datahiveorg.donetik.ui.navigation.TopBarAction

fun HomeScreen.toNavOptions(): NavOptions {
    return NavOptions(
        popUpToDestination = HomeScreen.Feed,
        inclusive = true,
        launchSingleTop = true
    )
}
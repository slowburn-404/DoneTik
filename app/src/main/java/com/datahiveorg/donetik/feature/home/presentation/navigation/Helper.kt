package com.datahiveorg.donetik.feature.home.presentation.navigation

import com.datahiveorg.donetik.ui.navigation.NavOptions

fun HomeScreen.toNavOptions(): NavOptions {
    return NavOptions(
        popUpToDestination = HomeScreen.Feed,
        inclusive = true,
        launchSingleTop = true
    )
}
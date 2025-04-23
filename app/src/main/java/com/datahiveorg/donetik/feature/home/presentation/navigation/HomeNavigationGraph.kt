package com.datahiveorg.donetik.feature.home.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.NavigatorFactory

fun NavGraphBuilder.homeNavigationGraph(
    navigatorFactory: NavigatorFactory,
    route: String,
    snackBarHostState: SnackbarHostState,

    ) {
    navigation(
        startDestination = HomeScreen.Feed.route,
        route = route
    ) {
        val homeNavigator = navigatorFactory.create<DoneTikNavigator>()


    }
}
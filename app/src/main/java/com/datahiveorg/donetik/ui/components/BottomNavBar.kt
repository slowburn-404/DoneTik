package com.datahiveorg.donetik.ui.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen
import com.datahiveorg.donetik.ui.navigation.FeatureScreen

@Composable
fun BottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    val bottomBarScreens = listOf(
        HomeScreen.Feed("")
    )

    BottomAppBar {
        bottomBarScreens.forEach { screen: FeatureScreen ->
            NavigationBarItem(
                icon = {
                    screen.iconRes?.let { iconId ->
                        painterResource(iconId)
                    }?.let { icon ->
                        Icon(
                            icon,
                            contentDescription = screen.title
                        )
                    }
                },
                label = {
                    Text(
                        screen.title
                    )
                },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationRoute!!) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}

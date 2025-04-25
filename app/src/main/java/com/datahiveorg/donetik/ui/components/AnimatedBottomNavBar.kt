package com.datahiveorg.donetik.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_SHORT

@Composable
fun AnimatedBottomNavBar(
    navController: NavHostController,
    currentDestination: NavDestination?
) {
    val bottomBarScreens = listOf(
        HomeScreen.Feed
    )
    val isBottomBarVisible = currentDestination?.route in listOf(
        HomeScreen.Feed.route
    )

    AnimatedVisibility(
        visible = isBottomBarVisible,
        enter = slideInVertically(
            tween(
                durationMillis = ANIMATION_DURATION_SHORT,
//                delayMillis = 1000
            )
        ) { it } + fadeIn(),
        exit = slideOutVertically {it} + fadeOut()
    ) {
        BottomAppBar {
            bottomBarScreens.forEach { screen: FeatureScreen ->
                NavigationBarItem(
                    icon = {
                        screen.bottomNavIconRes?.let { iconId ->
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
}

package com.datahiveorg.donetik.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
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
import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.core.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_SHORT

/**
 * A composable function that displays an animated bottom navigation bar.
 *
 * The bottom navigation bar appears and disappears with a slide and fade animation.
 * It displays a list of [FeatureScreen] items, allowing navigation between them.
 *
 * Example usage:
 * ```kotlin
 * @Composable
 * fun MyApp() {
 *     val navController = rememberNavController()
 *     val doneTikNavigator = remember(navController) { DoneTikNavigator(navController) }
 *     val navBackStackEntry by navController.currentBackStackEntryAsState()
 *     val currentDestination = navBackStackEntry?.destination
 *     var isBottomBarVisible by rememberSaveable { mutableStateOf(true) }
 *
 *     // Logic to determine when to show/hide the bottom bar based on the current route
 *     // For example, hide it on detail screens
 *     isBottomBarVisible = when (currentDestination?.route) {
 *         FeatureScreen.DetailScreen.route -> false // Example: Hide on DetailScreen
 *         else -> true
 *     }
 *
 *     val bottomBarScreens = listOf(
 *         FeatureScreen.HomeScreen,
 *         FeatureScreen.ProfileScreen,
 *         FeatureScreen.SettingsScreen
 *     )
 *
 *     Scaffold(
 *         bottomBar = {
 *             AnimatedBottomNavBar(
 *                 navigator = doneTikNavigator,
 *                 currentDestination = currentDestination,
 *                 isVisible = isBottomBarVisible,
 *                 bottomBarScreens = bottomBarScreens
 *             )
 *         }
 *     ) { paddingValues ->
 *         // Your app content using NavHost
 *         NavHost(
 *             navController = navController,
 *             startDestination = FeatureScreen.HomeScreen.route,
 *             modifier = Modifier.padding(paddingValues)
 *         ) {
 *             // Define your composable destinations
 */
@Composable
fun AnimatedBottomNavBar(
    navigator: DoneTikNavigator,
    currentDestination: NavDestination?,
    isVisible: Boolean,
    bottomBarScreens: List<FeatureScreen>
) {

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            tween(
                durationMillis = ANIMATION_DURATION_SHORT,
                delayMillis = ANIMATION_DURATION_SHORT,
                easing = EaseIn
            )
        ) { it } + fadeIn(
            tween(
                durationMillis = ANIMATION_DURATION_SHORT,
                delayMillis = ANIMATION_DURATION_SHORT,
                easing = EaseIn
            )
        ),
        exit = slideOutVertically(
            tween(
                durationMillis = ANIMATION_DURATION_SHORT,
                delayMillis = ANIMATION_DURATION_SHORT,
                easing = EaseOut
            )
        ) { it } + fadeOut(
            tween(
                durationMillis = ANIMATION_DURATION_SHORT,
                delayMillis = ANIMATION_DURATION_SHORT,
                easing = EaseIn
            )
        )
    ) {
        BottomAppBar {
            bottomBarScreens.forEach { screen: FeatureScreen ->
                NavigationBarItem(
                    icon = {
                        screen.screenUIConfig.bottomNavIconRes?.let { iconId ->
                            painterResource(iconId)
                        }?.let { icon ->
                            Icon(
                                painter = icon,
                                contentDescription = screen.screenUIConfig.title
                            )
                        }
                    },
                    label = {
                        if(screen.screenUIConfig.hasBottomBar) {
                            Text(
                                screen.screenUIConfig.title
                            )
                        }
                    },
                    selected = currentDestination?.route?.let { route ->
                        screen::class.simpleName.orEmpty() in route
                    } ?: false,
                    onClick = {
                        navigator.navigate(screen)
                    },
                    alwaysShowLabel = false
                )
            }
        }
    }
}

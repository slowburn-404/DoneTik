package com.datahiveorg.donetik.ui.components

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
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_SHORT

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
                                icon,
                                contentDescription = screen.screenUIConfig.title
                            )
                        }
                    },
                    label = {
                        Text(
                            screen.screenUIConfig.title
                        )
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

package com.datahiveorg.donetik.core.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.datahiveorg.donetik.util.Animation

/**
 * Data class that holds the UI configuration for a screen.
 * This class is used to define the appearance and behavior of a screen,
 * such as the title, whether it has a top app bar, bottom bar, FAB, etc.
 *
 * @param title The title of the screen.
 * @param hasTopAppBar Whether the screen has a top app bar.
 * @param hasBottomBar Whether the screen has a bottom bar.
 * @param hasFAB Whether the screen has a FAB.
 * @param hasNavIcon Whether the screen has a navigation icon.
 * @param topBarActions The list of actions to display in the top app bar.
 * @param bottomNavIconRes The resource ID of the bottom navigation icon.
 * @param navIconRes The resource ID of the navigation icon.
 * @param fabIconRes The resource ID of the FAB icon.
 * @param enterTransition The enter transition for the screen.
 * @param exitTransition The exit transition for the screen.
 */
@Stable
@Immutable
data class ScreenUIConfig(
    val title: String = "",
    val hasTopAppBar: Boolean = false,
    val hasBottomBar: Boolean = false,
    val hasFAB: Boolean = false,
    val hasNavIcon: Boolean = false,
    val topBarActions: List<TopBarAction> = emptyList(),
    @DrawableRes val bottomNavIconRes: Int? = null,
    @DrawableRes val navIconRes: Int? = null,
    @DrawableRes val fabIconRes: Int? = null,
    val enterTransition: EnterTransition = defaultEnterTransition(),
    val exitTransition: ExitTransition = defaultExitTransition()
)


fun defaultEnterTransition(): EnterTransition =
    slideInHorizontally(
        animationSpec = tween(
            delayMillis = Animation.ANIMATION_DURATION_SHORT,
            durationMillis = Animation.ANIMATION_DURATION_SHORT,
            easing = EaseIn
        )
    ) { it } + fadeIn(
        animationSpec = tween(
            durationMillis = Animation.ANIMATION_DURATION_SHORT,
            easing = LinearEasing
        )
    )

fun defaultExitTransition(): ExitTransition =
    slideOutHorizontally(
        animationSpec = tween(
            durationMillis = Animation.ANIMATION_DURATION_SHORT,
            delayMillis = Animation.ANIMATION_DURATION_SHORT,
            easing = EaseIn
        )
    ) { -it } + fadeOut(
        animationSpec = tween(
            durationMillis = Animation.ANIMATION_DURATION_SHORT,
            easing = LinearEasing
        )
    )

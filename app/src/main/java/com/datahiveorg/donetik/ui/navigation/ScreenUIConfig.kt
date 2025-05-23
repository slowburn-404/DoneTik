package com.datahiveorg.donetik.ui.navigation

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
import com.datahiveorg.donetik.util.Animation

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
            easing = LinearEasing
        )
    ) { -it } + fadeOut(
        animationSpec = tween(
            durationMillis = Animation.ANIMATION_DURATION_SHORT,
            easing = LinearEasing
        )
    )

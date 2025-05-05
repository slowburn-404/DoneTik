package com.datahiveorg.donetik.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.datahiveorg.donetik.util.Animation

/**
 * Animate navigation transitions
 */
inline fun <reified R : Any> NavGraphBuilder.animatedComposable(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable<R>(
    deepLinks = deepLinks,
    enterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(
                delayMillis = Animation.ANIMATION_DURATION_LONG,
                durationMillis = Animation.ANIMATION_DURATION_LONG,
                easing = EaseIn
            )
        ) +
                fadeIn(
                    animationSpec = tween(
                        delayMillis = Animation.ANIMATION_DURATION_LONG,
                        durationMillis = Animation.ANIMATION_DURATION_LONG,
                        easing = LinearEasing
                    )
                )

    },
    exitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(
                durationMillis = Animation.ANIMATION_DURATION_LONG,
                delayMillis = Animation.ANIMATION_DURATION_LONG,
                easing = LinearEasing
            )
        ) +
                fadeOut(
                    animationSpec = tween(
                        delayMillis = Animation.ANIMATION_DURATION_LONG,
                        durationMillis = Animation.ANIMATION_DURATION_LONG,
                        easing = LinearEasing
                    ),
                )


    },
    content = content
)
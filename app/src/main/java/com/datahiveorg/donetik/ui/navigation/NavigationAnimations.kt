package com.datahiveorg.donetik.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.datahiveorg.donetik.util.Animation.ANIMATION_DURATION_LONG

/**
 * Animate navigation transitions
 */

fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable(
    route = route,
    arguments = arguments,
    deepLinks = deepLinks,
    enterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(ANIMATION_DURATION_LONG, easing = EaseIn)
        ) +
                fadeIn(
                    animationSpec = tween(ANIMATION_DURATION_LONG, easing = LinearEasing)
                )

    },
    exitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(ANIMATION_DURATION_LONG)
        ) +
                fadeOut(
                    animationSpec = tween(ANIMATION_DURATION_LONG, easing = LinearEasing),
                )


    },
    content = content
)
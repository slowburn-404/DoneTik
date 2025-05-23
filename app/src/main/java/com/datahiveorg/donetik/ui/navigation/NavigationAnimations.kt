package com.datahiveorg.donetik.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * Animate navigation transitions
 */
inline fun <reified S : FeatureScreen> NavGraphBuilder.animatedComposable(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable<S>(
    deepLinks = deepLinks,
    enterTransition = {
        S::class.objectInstance?.screenUIConfig?.enterTransition ?: defaultEnterTransition()
    },
    exitTransition = {
        S::class.objectInstance?.screenUIConfig?.exitTransition ?: defaultExitTransition()
    },
    popEnterTransition = {
        S::class.objectInstance?.screenUIConfig?.enterTransition ?: defaultEnterTransition()

    },
    popExitTransition = {
        S::class.objectInstance?.screenUIConfig?.exitTransition ?: defaultExitTransition()
    },
    content = content
)
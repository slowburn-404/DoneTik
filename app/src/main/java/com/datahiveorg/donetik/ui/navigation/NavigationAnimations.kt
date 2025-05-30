package com.datahiveorg.donetik.ui.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


/**
 * Add a destination to the [NavGraphBuilder] that will be animated when navigating to and from it.
 *
 * This is a convenience function that wraps the [composable] function and provides default
 * animations for entering and exiting the destination.
 *
 * The animations can be customized by providing a [ScreenUIConfig] for the destination.
 *
 * @param D The type of the destination. Must be a subclass of [FeatureScreen].
 * @param deepLinks A list of deep links that will navigate to this destination.
 * @param content The content of the destination.
 */
inline fun <reified D : FeatureScreen> NavGraphBuilder.animatedComposable(
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) = composable<D>(
    deepLinks = deepLinks,
    enterTransition = {
        D::class.objectInstance?.screenUIConfig?.enterTransition ?: defaultEnterTransition()
    },
    exitTransition = {
        D::class.objectInstance?.screenUIConfig?.exitTransition ?: defaultExitTransition()
    },
    popEnterTransition = {
        D::class.objectInstance?.screenUIConfig?.enterTransition ?: defaultEnterTransition()

    },
    popExitTransition = {
        D::class.objectInstance?.screenUIConfig?.exitTransition ?: defaultExitTransition()
    },
    content = content
)
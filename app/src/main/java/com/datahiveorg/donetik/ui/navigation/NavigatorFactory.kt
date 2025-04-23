package com.datahiveorg.donetik.ui.navigation

import androidx.navigation.NavHostController
import org.koin.core.Koin
import org.koin.core.parameter.parametersOf

/**
 * A factory class for creating navigation-related instances using Koin dependency injection.
 *
 * @property koin The [Koin] instance used for resolving dependencies.
 * @property navController The [NavHostController] that is passed as a parameter to createdAt instances.
 */

class NavigatorFactory(
    val koin: Koin,
    val navController: NavHostController
) {
    inline fun <reified T : Any> create(): T {
        return koin.get<T> { parametersOf(navController) }
    }
}
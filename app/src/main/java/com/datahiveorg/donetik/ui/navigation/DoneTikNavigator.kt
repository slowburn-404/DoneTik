package com.datahiveorg.donetik.ui.navigation

import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import kotlin.reflect.KClass

interface DoneTikNavigator {
    fun navigate(destination: FeatureScreen, navOptions: NavOptions)

    fun navigateUp()
}

class DoneTikNavigatorImpl(private val navController: NavHostController) : DoneTikNavigator {

    override fun navigate(destination: FeatureScreen, navOptions: NavOptions) {
        navController.navigate(destination) {
            this.launchSingleTop = navOptions.launchSingleTop

            navOptions.popUpToDestination?.let { screen ->
                val startDestinationId = when (screen) {
                    is HomeFeature -> findGraphStartDestinationId(HomeFeature::class)
                    is AuthFeature -> findGraphStartDestinationId(AuthFeature::class)
                    is OnBoardingFeature -> findGraphStartDestinationId(OnBoardingFeature::class)
                    is RouterScreen -> findGraphStartDestinationId(RouterScreen::class)
                    else -> null
                }
                startDestinationId?.let { id ->
                    popUpTo(id) {
                        this.saveState = navOptions.saveState
                        this.inclusive = navOptions.inclusive
                    }
                    this.restoreState = navOptions.restoreState
                }
            }

        }
    }

    override fun navigateUp() {
        navController.navigateUp()
    }

    private fun findGraphStartDestinationId(screenClass: KClass<out FeatureScreen>): Int? {
        val graphNode = navController.graph.findNode(screenClass) as? NavGraph
        return graphNode?.startDestinationId
    }
}





package com.datahiveorg.donetik.core.ui.navigation

import androidx.navigation.NavHostController

/**
 * Interface for navigation within the DoneTik application.
 *
 * This interface defines the contract for navigating between different screens (Features)
 * within the application. It abstracts the underlying navigation mechanism, allowing for
 * easier testing and potential future changes to the navigation library.
 */
interface DoneTikNavigator {
    fun navigate(destination: FeatureScreen)

    fun navigateUp()
}

class DoneTikNavigatorImpl(private val navController: NavHostController) : DoneTikNavigator {

    override fun navigate(destination: FeatureScreen) {
        navController.navigate(destination) {
            launchSingleTop = true
            destination.buildNavOptions(this)
        }
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}





package com.datahiveorg.donetik.ui.navigation

import androidx.navigation.NavHostController

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





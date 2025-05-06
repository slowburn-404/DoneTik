package com.datahiveorg.donetik.ui.navigation

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.feature.auth.presentation.navigation.AuthenticationScreen
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen

interface DoneTikNavigator {
    fun navigate(destination: FeatureScreen, navOptions: NavOptions)

    fun navigateUp()
}

class DoneTikNavigatorImpl(private val navController: NavHostController) : DoneTikNavigator {

    override fun navigate(destination: FeatureScreen, navOptions: NavOptions) {
        val isFromRouterScreen =
            navController
                .currentBackStackEntry
                ?.destination
                ?.route
                ?.contains(RouterScreen.javaClass.simpleName) == true

        navController.navigate(destination) {
            this.launchSingleTop = navOptions.launchSingleTop

            if (isFromRouterScreen && destination !is RouterScreen) {
                popUpTo<RouterScreen> {
                    inclusive = true
                    saveState = false
                }
                this.restoreState = false

            } else if (navOptions.popUpToDestination != null) {
                when (navOptions.popUpToDestination) {
                    is HomeScreen -> {
                        popUpTo<HomeScreen.Feed> {
                            this.saveState = navOptions.saveState
                            this.inclusive = navOptions.inclusive
                        }
                    }

                    is AuthenticationScreen -> {
                        popUpTo<AuthenticationScreen.LoginScreen> {
                            this.saveState = navOptions.saveState
                            this.inclusive = navOptions.inclusive
                        }
                    }

                    is RouterScreen -> {
                        popUpTo<RouterScreen> {
                            this.inclusive = navOptions.inclusive
                        }
                    }
                }
            }
            this.restoreState = navOptions.restoreState && !navOptions.inclusive

        }
    }

    override fun navigateUp() {
        navController.navigateUp()
    }
}





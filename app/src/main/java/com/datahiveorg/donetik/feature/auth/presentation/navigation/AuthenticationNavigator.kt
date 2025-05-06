package com.datahiveorg.donetik.feature.auth.presentation.navigation

import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeScreen
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.NavOptions

interface AuthenticationNavigator {
    fun navigateToLogin()

    fun navigateToSignUp()

    fun navigateToHomeFeature()
}

class AuthenticationNavigatorImpl(
    private val doneTikNavigator: DoneTikNavigator
) : AuthenticationNavigator {

    override fun navigateToLogin() {
        val loginScreenNavOptions = NavOptions(
            inclusive = true,
            launchSingleTop = true,
            popUpToDestination = AuthenticationScreen.LoginScreen
        )
        doneTikNavigator.navigate(
            destination = AuthenticationScreen.LoginScreen,
            navOptions = loginScreenNavOptions
        )
    }

    override fun navigateToSignUp() {
        val signUpScreenNavOptions = NavOptions(
            launchSingleTop = true,
            inclusive = true,
            popUpToDestination = AuthenticationScreen.SignUpScreen
        )
        doneTikNavigator.navigate(
            destination = AuthenticationScreen.LoginScreen,
            navOptions = signUpScreenNavOptions
        )
    }

    override fun navigateToHomeFeature() {
        val homeScreenNavOptions = NavOptions(
            launchSingleTop = true,
            inclusive = true,
            popUpToDestination = HomeScreen.Feed
        )

        doneTikNavigator.navigate(
            destination = HomeScreen.Feed,
            navOptions = homeScreenNavOptions
        )
    }

}
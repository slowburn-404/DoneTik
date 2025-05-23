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
        doneTikNavigator.navigate(
            destination = AuthenticationScreen.LoginScreen
        )
    }

    override fun navigateToSignUp() {
        doneTikNavigator.navigate(
            destination = AuthenticationScreen.LoginScreen
        )
    }

    override fun navigateToHomeFeature() {
        doneTikNavigator.navigate(
            destination = HomeScreen.Feed
        )
    }

}
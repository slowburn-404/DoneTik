package com.datahiveorg.donetik.feature.auth.presentation.navigation

import com.datahiveorg.donetik.feature.home.presentation.navigation.Feed
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator

interface AuthenticationNavigator {
    fun navigateToLogin()

    fun navigateToSignUp()

    fun navigateToFeed()
}

class AuthenticationNavigatorImpl(
    private val doneTikNavigator: DoneTikNavigator
) : AuthenticationNavigator {

    override fun navigateToLogin() {
        doneTikNavigator.navigate(
            destination = LoginScreen
        )
    }

    override fun navigateToSignUp() {
        doneTikNavigator.navigate(
            destination = LoginScreen
        )
    }

    override fun navigateToFeed() {
        doneTikNavigator.navigate(
            destination = Feed
        )
    }

}
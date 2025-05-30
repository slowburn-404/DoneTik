package com.datahiveorg.donetik.feature.auth.presentation.navigation

import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.HomeFeature

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
            destination = LoginScreen
        )
    }

    override fun navigateToSignUp() {
        doneTikNavigator.navigate(
            destination = SignUpScreen
        )
    }

    override fun navigateToHomeFeature() {
        doneTikNavigator.navigate(
            destination = HomeFeature
        )
    }

}
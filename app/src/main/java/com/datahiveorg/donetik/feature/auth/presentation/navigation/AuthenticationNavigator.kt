package com.datahiveorg.donetik.feature.auth.presentation.navigation

import androidx.navigation.NavHostController

interface AuthenticationNavigator {
    fun navigate(screen: AuthenticationScreen)
}

internal class AuthenticationNavigatorImpl(
    private val navController: NavHostController
) : AuthenticationNavigator {

    override fun navigate(screen: AuthenticationScreen) {
        when (screen) {
            is AuthenticationScreen.LoginScreen -> navigateToLogin()
            is AuthenticationScreen.SignUpScreen -> navigateToSignUp()
        }
    }

    private fun navigateToLogin() {
        navController.navigate(AuthenticationScreen.LoginScreen.route) {
            launchSingleTop = true
            popUpTo(AuthenticationScreen.LoginScreen.route) {
                inclusive = true
            }
        }
    }

    private fun navigateToSignUp() {
        navController.navigate(AuthenticationScreen.SignUpScreen.route) {
            launchSingleTop = true
            popUpTo(AuthenticationScreen.SignUpScreen.route) {
                inclusive = true
            }
        }
    }

}
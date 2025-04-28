package com.datahiveorg.donetik.feature.auth.presentation.navigation

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.FeatureScreen

internal class AuthenticationNavigatorImpl(
    private val navController: NavHostController
) : DoneTikNavigator {

    override fun navigate(screen: FeatureScreen) {
        when (screen) {
            is AuthenticationScreen.LoginScreen -> navigateToLogin()
            is AuthenticationScreen.SignUpScreen -> navigateToSignUp()
        }
    }

    private fun navigateToLogin() {
        navController.navigate(AuthenticationScreen.LoginScreen) {
            launchSingleTop = true
            popUpTo<AuthenticationScreen.LoginScreen> {
                inclusive = true
            }
        }
    }

    private fun navigateToSignUp() {
        navController.navigate(AuthenticationScreen.SignUpScreen) {
            launchSingleTop = true
            popUpTo<AuthenticationScreen.SignUpScreen> {
                inclusive = true
            }
        }
    }

}
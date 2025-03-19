package com.datahiveorg.donetik.feature.auth.presentation.navigation

import com.datahiveorg.donetik.ui.navigation.FeatureScreen

sealed class AuthenticationScreen(
    override val route: String,
    override val title: String
): FeatureScreen {
    data object LoginScreen : AuthenticationScreen(
        route = "login_screen",
        title = "Login"
    )
   data object SignUpScreen: AuthenticationScreen(
       route = "sign_up_screen",
       title = "Sign Up"
   )
}
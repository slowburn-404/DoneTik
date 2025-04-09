package com.datahiveorg.donetik.feature.auth.presentation.navigation

import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthenticationScreen(
    override val route: String,
    override val title: String
) : FeatureScreen {
    @Serializable
    data object LoginScreen : AuthenticationScreen(
        route = "login_screen",
        title = "Login"
    )

    @Serializable
    data object SignUpScreen : AuthenticationScreen(
        route = "sign_up_screen",
        title = "Sign Up"
    )
}
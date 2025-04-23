package com.datahiveorg.donetik.feature.auth.presentation.navigation

import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.serialization.Serializable

@Serializable
sealed class AuthenticationScreen : FeatureScreen {
    @Serializable
    data object LoginScreen : AuthenticationScreen(
    ) {
        override val route: String
            get() = "login_screen"
        override val title: String
            get() = "Login"
    }

    @Serializable
    data object SignUpScreen : AuthenticationScreen(
    ) {
        override val route: String
            get() = "sign_up_screen"
        override val title: String
            get() = "Sign Up"
    }
}
package com.datahiveorg.donetik.feature.auth.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.ui.navigation.AuthFeature
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.serialization.Serializable

sealed class AuthenticationScreen : FeatureScreen {
    @Serializable
    data object LoginScreen : AuthenticationScreen(
    ) {
        override fun buildNavOptions(builder: NavOptionsBuilder) {
            builder.popUpTo<AuthFeature> {
                inclusive = true
            }
        }
        override val title: String
            get() = "Login"
    }

    @Serializable
    data object SignUpScreen : AuthenticationScreen(
    ) {
        override fun buildNavOptions(builder: NavOptionsBuilder) {
            builder.popUpTo<AuthFeature> {
                inclusive = true
            }
        }
        override val title: String
            get() = "Sign Up"
    }
}
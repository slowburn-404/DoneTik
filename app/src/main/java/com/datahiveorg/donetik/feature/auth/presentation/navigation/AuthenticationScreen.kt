package com.datahiveorg.donetik.feature.auth.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.ui.navigation.ScreenUIConfig
import kotlinx.serialization.Serializable

@Serializable
data object LoginScreen : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<LoginScreen> {
            inclusive = true
        }
    }

    override val screenUIConfig: ScreenUIConfig
        get() = super.screenUIConfig
}

@Serializable
data object SignUpScreen : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<SignUpScreen> {
            inclusive = true
        }
    }

    override val screenUIConfig: ScreenUIConfig
        get() = super.screenUIConfig
}
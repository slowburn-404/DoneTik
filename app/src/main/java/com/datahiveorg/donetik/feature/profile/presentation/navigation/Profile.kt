package com.datahiveorg.donetik.feature.profile.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.core.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.core.ui.navigation.HomeFeature
import com.datahiveorg.donetik.core.ui.navigation.ScreenUIConfig
import kotlinx.serialization.Serializable
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.feature.home.presentation.navigation.homeEnterTransition
import com.datahiveorg.donetik.feature.home.presentation.navigation.homeExitTransition

@Serializable
object Profile: FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
       builder.popUpTo<HomeFeature> {
           inclusive = false
       }
    }

    override val screenUIConfig: ScreenUIConfig
        get() = ScreenUIConfig(
            title = "Profile",
            hasBottomBar = true,
            hasTopAppBar = true,
            hasFAB = true,
            bottomNavIconRes = R.drawable.ic_profile,
            fabIconRes = R.drawable.ic_edit,
            enterTransition = homeEnterTransition(),
            exitTransition = homeExitTransition()
        )
}

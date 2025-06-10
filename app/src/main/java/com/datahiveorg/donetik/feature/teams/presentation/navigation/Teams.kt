package com.datahiveorg.donetik.feature.teams.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.core.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.core.ui.navigation.HomeFeature
import com.datahiveorg.donetik.core.ui.navigation.ScreenUIConfig
import com.datahiveorg.donetik.core.ui.navigation.TeamsFeature
import com.datahiveorg.donetik.feature.home.presentation.navigation.homeEnterTransition
import com.datahiveorg.donetik.feature.home.presentation.navigation.homeExitTransition
import kotlinx.serialization.Serializable

@Serializable
object Teams : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<HomeFeature> {
            inclusive = false
        }
    }

    override val screenUIConfig: ScreenUIConfig
        get() = ScreenUIConfig(
            title = "Teams",
            hasBottomBar = true,
            hasTopAppBar = true,
            hasFAB = true,
            bottomNavIconRes = R.drawable.ic_teams,
            exitTransition = homeExitTransition(),
            enterTransition = homeEnterTransition()

        )
}
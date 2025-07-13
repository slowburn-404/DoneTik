package com.datahiveorg.donetik.feature.leaderboard.presentation.navigation

import androidx.navigation.NavOptionsBuilder
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.core.ui.navigation.FeatureScreen
import com.datahiveorg.donetik.core.ui.navigation.HomeFeature
import com.datahiveorg.donetik.core.ui.navigation.ScreenUIConfig
import com.datahiveorg.donetik.feature.home.presentation.navigation.homeEnterTransition
import com.datahiveorg.donetik.feature.home.presentation.navigation.homeExitTransition
import kotlinx.serialization.Serializable

@Serializable
data object LeaderBoard : FeatureScreen {
    override fun buildNavOptions(builder: NavOptionsBuilder) {
        builder.popUpTo<HomeFeature> {
            inclusive = false
            saveState = true
        }
        builder.restoreState = true
    }

    override val screenUIConfig: ScreenUIConfig
        get() = ScreenUIConfig(
            title = "Leaderboard",
            hasTopAppBar = true,
            hasBottomBar = true,
            hasFAB = true,
            bottomNavIconRes = R.drawable.ic_leaderboard,
            navIconRes = R.drawable.ic_arrow_back,
            enterTransition = homeEnterTransition(),
            exitTransition = homeExitTransition()
        )
}
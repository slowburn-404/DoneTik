package com.datahiveorg.donetik.feature.leaderboard.di

import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.feature.leaderboard.presentation.navigation.LeaderBoardNavigator
import com.datahiveorg.donetik.feature.leaderboard.presentation.navigation.LeaderBoardNavigatorImpl
import org.koin.dsl.module

val leaderBoardModule = module {
    factory<LeaderBoardNavigator> { (navigator: DoneTikNavigator) ->
        LeaderBoardNavigatorImpl(
            navigator
        )
    }
}
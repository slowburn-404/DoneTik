package com.datahiveorg.donetik.feature.leaderboard.presentation.navigation

import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator

interface LeaderBoardNavigator {
    fun navigateToLeaderBoard()
}

class LeaderBoardNavigatorImpl(
    private val doneTikNavigator: DoneTikNavigator
) : LeaderBoardNavigator {
    override fun navigateToLeaderBoard() {
        doneTikNavigator.navigate(destination = LeaderBoard)
    }
}
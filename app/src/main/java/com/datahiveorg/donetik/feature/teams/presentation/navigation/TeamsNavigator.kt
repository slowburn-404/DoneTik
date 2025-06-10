package com.datahiveorg.donetik.feature.teams.presentation.navigation

import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator

interface TeamsNavigator {
    fun navigateToTeams()
}

class TeamsNavigatorImpl(
    private val doneTikNavigator: DoneTikNavigator
) : TeamsNavigator {
    override fun navigateToTeams() {
        doneTikNavigator.navigate(destination = Teams)
    }
}
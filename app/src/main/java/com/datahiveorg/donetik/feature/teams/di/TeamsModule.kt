package com.datahiveorg.donetik.feature.teams.di

import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.feature.teams.presentation.navigation.TeamsNavigator
import com.datahiveorg.donetik.feature.teams.presentation.navigation.TeamsNavigatorImpl
import org.koin.dsl.module


val teamsModule = module {
    factory<TeamsNavigator> { (doneTikNavigator: DoneTikNavigator) ->
        TeamsNavigatorImpl(doneTikNavigator)
    }

}
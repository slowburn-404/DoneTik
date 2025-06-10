package com.datahiveorg.donetik.feature.teams.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.datahiveorg.donetik.core.ui.navigation.TeamsFeature
import com.datahiveorg.donetik.core.ui.navigation.animatedComposable
import com.datahiveorg.donetik.feature.teams.presentation.TeamsScreen

fun NavGraphBuilder.teamsNavGraph(
    teamsNavigator: TeamsNavigator,
    snackBarHostState: SnackbarHostState
) {
    navigation<TeamsFeature>(
        startDestination = Teams
    ) {
        animatedComposable<Teams> {
            TeamsScreen(
                teamsNavigator = teamsNavigator,
                snackBarHostState = snackBarHostState
            )

        }
    }
}
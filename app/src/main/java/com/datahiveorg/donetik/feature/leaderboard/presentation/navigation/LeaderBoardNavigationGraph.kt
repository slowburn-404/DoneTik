package com.datahiveorg.donetik.feature.leaderboard.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.datahiveorg.donetik.core.ui.navigation.LeaderBoardFeature
import com.datahiveorg.donetik.core.ui.navigation.animatedComposable
import com.datahiveorg.donetik.feature.leaderboard.presentation.LeaderBoardScreen
import com.datahiveorg.donetik.feature.leaderboard.presentation.LeaderBoardViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.leaderBoardNavGraph(
    leaderBoardNavigator: LeaderBoardNavigator,
    snackBarHostState: SnackbarHostState

) {
    navigation<LeaderBoardFeature>(
        startDestination = LeaderBoard,
    ) {
        animatedComposable<LeaderBoard> {
            LeaderBoardScreen(
                navigator = leaderBoardNavigator,
                snackBarHostState = snackBarHostState,
                viewModel = koinViewModel<LeaderBoardViewModel>()
            )
        }
    }
}
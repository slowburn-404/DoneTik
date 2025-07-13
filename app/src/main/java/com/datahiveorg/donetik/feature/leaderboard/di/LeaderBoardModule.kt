package com.datahiveorg.donetik.feature.leaderboard.di

import com.datahiveorg.donetik.core.firebase.authentication.AuthDataSource
import com.datahiveorg.donetik.core.firebase.firestore.LeaderBoardDataSource
import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.feature.leaderboard.data.LeaderBoardRepositoryImpl
import com.datahiveorg.donetik.feature.leaderboard.domain.LeaderBoardRepository
import com.datahiveorg.donetik.feature.leaderboard.presentation.LeaderBoardViewModel
import com.datahiveorg.donetik.feature.leaderboard.presentation.navigation.LeaderBoardNavigator
import com.datahiveorg.donetik.feature.leaderboard.presentation.navigation.LeaderBoardNavigatorImpl
import com.datahiveorg.donetik.util.DispatcherProvider
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val leaderBoardModule = module {
    factory<LeaderBoardNavigator> { (navigator: DoneTikNavigator) ->
        LeaderBoardNavigatorImpl(
            navigator
        )
    }

    factory<LeaderBoardRepository> {
        LeaderBoardRepositoryImpl(
            leaderBoardDataSource = get<LeaderBoardDataSource>(),
            authDataSource = get<AuthDataSource>(),
            dispatcherProvider = get<DispatcherProvider>()
        )
    }

    viewModel<LeaderBoardViewModel> {
        LeaderBoardViewModel(
            leaderBoardRepository = get<LeaderBoardRepository>()
        )
    }
}
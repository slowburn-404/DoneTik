package com.datahiveorg.donetik.di

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.MainActivityViewModel
import com.datahiveorg.donetik.core.datastore.di.datastoreModule
import com.datahiveorg.donetik.feature.auth.di.authModule
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.feature.home.di.homeModule
import com.datahiveorg.donetik.feature.onboarding.data.OnBoardingRepository
import com.datahiveorg.donetik.feature.onboarding.di.onBoardingModule
import com.datahiveorg.donetik.feature.router.RouterViewModel
import com.datahiveorg.donetik.core.firebase.di.firebaseModule
import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigatorImpl
import com.datahiveorg.donetik.feature.leaderboard.di.leaderBoardModule
import com.datahiveorg.donetik.util.dispatcherModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * The main Koin module for the application.
 *
 * This module includes all other modules in the application, as well as providing
 * dependencies for the [RouterViewModel], [DoneTikNavigator], and [MainActivityViewModel].
 */
val appModule = module {
    includes(
        firebaseModule,
        dispatcherModule,
        datastoreModule,
        authModule,
        onBoardingModule,
        homeModule,
        leaderBoardModule
    )

    viewModel<RouterViewModel> {
        RouterViewModel(
            authRepository = get<AuthRepository>(),
            onBoardingRepository = get<OnBoardingRepository>()
        )
    }

    factory<DoneTikNavigator> { (navController: NavHostController) ->
        DoneTikNavigatorImpl(
            navController = navController
        )
    }

    viewModel<MainActivityViewModel> {
        MainActivityViewModel(
            authRepository = get<AuthRepository>()
        )
    }
}
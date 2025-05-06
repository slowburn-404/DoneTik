package com.datahiveorg.donetik.di

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.datastore.di.datastoreModule
import com.datahiveorg.donetik.feature.auth.di.authModule
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.feature.home.di.homeModule
import com.datahiveorg.donetik.feature.onboarding.data.OnBoardingRepository
import com.datahiveorg.donetik.feature.onboarding.di.onBoardingModule
import com.datahiveorg.donetik.feature.router.RouterViewModel
import com.datahiveorg.donetik.firebase.di.firebaseModule
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigatorImpl
import com.datahiveorg.donetik.util.dispatcherModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    includes(
        firebaseModule,
        dispatcherModule,
        datastoreModule,
        authModule,
        onBoardingModule,
        homeModule,
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
}
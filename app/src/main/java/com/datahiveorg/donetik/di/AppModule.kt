package com.datahiveorg.donetik.di

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.datastore.di.datastoreModule
import com.datahiveorg.donetik.feature.auth.di.authModule
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.feature.onboarding.data.OnBoardingRepository
import com.datahiveorg.donetik.feature.onboarding.di.onBoardingModule
import com.datahiveorg.donetik.router.RouterViewModel
import com.datahiveorg.donetik.ui.navigation.NavigatorFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    includes(authModule, datastoreModule, onBoardingModule)

    factory { (navHostController: NavHostController) ->
        NavigatorFactory(
            navController = navHostController,
            koin = getKoin()
        )
    }

    viewModel<RouterViewModel> {
        RouterViewModel(
            authRepository = get<AuthRepository>(),
            onBoardingRepository = get<OnBoardingRepository>()
        )
    }
}
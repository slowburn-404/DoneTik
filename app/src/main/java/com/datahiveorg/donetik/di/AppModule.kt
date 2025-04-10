package com.datahiveorg.donetik.di

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.feature.auth.di.authModule
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.router.RouterViewModel
import com.datahiveorg.donetik.ui.navigation.NavigatorFactory
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    includes(authModule)

    factory { (navHostController: NavHostController) ->
        NavigatorFactory(
            navHostController = navHostController,
            koin = getKoin()
        )
    }

    viewModel<RouterViewModel> {
        RouterViewModel(
            authRepository = get<AuthRepository>()
        )
    }
}
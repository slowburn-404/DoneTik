package com.datahiveorg.donetik.feature.home.di

import androidx.navigation.NavHostController
import com.datahiveorg.donetik.feature.home.data.HomeRepositoryImpl
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.presentation.HomeViewModel
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigatorImpl
import com.datahiveorg.donetik.firebase.firestore.FirebaseFireStoreService
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single<HomeRepository> {
        HomeRepositoryImpl(
            fireStoreService = get<FirebaseFireStoreService>()
        )
    }

    viewModel { (userId: String?) ->
        HomeViewModel(
            homeRepository = get<HomeRepository>(),
            userId = userId
        )

    }

    factory<HomeNavigator> { (navController: NavHostController) ->
        HomeNavigatorImpl(
            navController = navController
        )
    }

}
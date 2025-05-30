package com.datahiveorg.donetik.feature.home.di

import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.feature.home.data.HomeRepositoryImpl
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.usecase.GetUserInfoUseCase
import com.datahiveorg.donetik.feature.home.domain.usecase.GetUserInfoUseCaseImpl
import com.datahiveorg.donetik.feature.home.presentation.feed.FeedViewModel
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigator
import com.datahiveorg.donetik.feature.home.presentation.navigation.HomeNavigatorImpl
import com.datahiveorg.donetik.feature.home.presentation.newtask.NewTaskViewModel
import com.datahiveorg.donetik.feature.home.presentation.taskview.TaskViewModel
import com.datahiveorg.donetik.firebase.firestore.FireStoreDataSource
import com.datahiveorg.donetik.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.util.DispatcherProvider
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module for providing dependencies related to the home feature.
 *
 * This module defines how to create instances of repositories, view models,
 * use cases, and navigators specific to the home functionality of the application.
 */
val homeModule = module {
    factory<HomeRepository> {
        HomeRepositoryImpl(
            fireStoreDataSource = get<FireStoreDataSource>()
        )
    }

    viewModel<FeedViewModel> {
        FeedViewModel(
            homeRepository = get<HomeRepository>(),
            getUserInfoUseCase = get<GetUserInfoUseCase>(),
            dispatcher = get<DispatcherProvider>()
        )

    }

    viewModel<NewTaskViewModel> {
        NewTaskViewModel(
            homeRepository = get<HomeRepository>(),
            getUserInfoUseCase = get<GetUserInfoUseCase>()
        )
    }

    viewModel<TaskViewModel> {
        TaskViewModel(
            homeRepository = get<HomeRepository>(),
        )
    }

    factory<HomeNavigator> { (doneTikNavigator: DoneTikNavigator) ->
        HomeNavigatorImpl(
            doneTikNavigator = doneTikNavigator
        )
    }

    factory<GetUserInfoUseCase> {
        GetUserInfoUseCaseImpl(
            authRepository = get<AuthRepository>()
        )
    }
}
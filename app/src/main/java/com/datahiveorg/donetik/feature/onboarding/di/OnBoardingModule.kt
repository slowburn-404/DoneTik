package com.datahiveorg.donetik.feature.onboarding.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.datahiveorg.donetik.datastore.di.datastoreModule
import com.datahiveorg.donetik.feature.onboarding.data.OnBoardingRepository
import com.datahiveorg.donetik.feature.onboarding.data.OnBoardingRepositoryImpl
import com.datahiveorg.donetik.feature.onboarding.presentation.OnBoardingViewModel
import com.datahiveorg.donetik.util.DispatcherProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val onBoardingModule = module {
    includes(datastoreModule)

    viewModel<OnBoardingViewModel> {
        OnBoardingViewModel(
            onBoardingRepository = get<OnBoardingRepository>(),
        )
    }

    single<OnBoardingRepository> {
        OnBoardingRepositoryImpl(
            dataStore = get<DataStore<Preferences>>(),
            dispatcherProvider = get<DispatcherProvider>()
        )
    }
}
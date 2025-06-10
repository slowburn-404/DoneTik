package com.datahiveorg.donetik.feature.profile.di

import com.datahiveorg.donetik.core.ui.navigation.DoneTikNavigator
import com.datahiveorg.donetik.feature.profile.presentation.navigation.ProfileNavigator
import com.datahiveorg.donetik.feature.profile.presentation.navigation.ProfileNavigatorImpl
import org.koin.dsl.module

val profileModule = module {
    factory<ProfileNavigator> { (doneTikNavigator: DoneTikNavigator) ->
        ProfileNavigatorImpl(doneTikNavigator)
    }
}
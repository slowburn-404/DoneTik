package com.datahiveorg.donetik.feature.home.di

import com.datahiveorg.donetik.feature.home.data.HomeRepositoryImpl
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.firebase.firestore.FirebaseFireStoreService
import org.koin.dsl.module

val homeModule = module {
    single<HomeRepository> {
        HomeRepositoryImpl(
            fireStoreService = get<FirebaseFireStoreService>()
        )
    }
}
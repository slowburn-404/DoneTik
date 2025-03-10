package com.datahiveorg.donetik.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

interface DispatcherProvider {
    val io: CoroutineDispatcher
}

class DispatcherProviderImpl: DispatcherProvider {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

}

val dispatcherModule = module {
    single<DispatcherProvider> { DispatcherProviderImpl() }

}
package com.datahiveorg.donetik.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

interface DispatcherProvider {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

class DispatcherProviderImpl: DispatcherProvider {
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO

    override val default: CoroutineDispatcher
        get() = Dispatchers.Default

}

val dispatcherModule = module {
    single<DispatcherProvider> { DispatcherProviderImpl() }
}
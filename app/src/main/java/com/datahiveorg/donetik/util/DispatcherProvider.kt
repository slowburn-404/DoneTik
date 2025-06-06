package com.datahiveorg.donetik.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

/**
 * Provides access to different coroutine dispatchers.
 * This interface allows for easier testing by enabling the injection of mock dispatchers.
 */
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

/**
 * Koin module for providing [DispatcherProvider] implementation.
 * This module registers a singleton instance of [DispatcherProviderImpl]
 * which can then be injected into other components that require access
 * to CoroutineDispatchers (IO and Default).
 */
val dispatcherModule = module {
    single<DispatcherProvider> { DispatcherProviderImpl() }
}
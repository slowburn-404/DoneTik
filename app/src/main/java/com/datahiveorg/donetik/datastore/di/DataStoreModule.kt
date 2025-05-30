package com.datahiveorg.donetik.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.datahiveorg.donetik.DoneTikApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


/**
 * Extension property for [Context] to access the application's [DataStore] instance
 * for storing and retrieving preferences.
 *
 * This DataStore is named "donetik_preferences".
 */
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "donetik_preferences")

/**
 * Provides an instance of DataStore<Preferences>.
 *
 * This function utilizes the extension property `dataStore` on the [Context] class,
 * which is configured using `preferencesDataStore` to create or retrieve a
 * DataStore instance named "donetik_preferences".
 *
 * @param context The application context used to access the DataStore.
 * @return An instance of DataStore<Preferences>.
 */
private fun provideDataStore(context: Context): DataStore<Preferences> = context.dataStore


/**
 * Koin module for providing the DataStore instance.
 *
 * This module defines a singleton binding for `DataStore<Preferences>`,
 * ensuring that only one instance of the DataStore is created and used
 * throughout the application.
 *
 * The `provideDataStore` function is used to create the DataStore instance,
 * which internally uses the `preferencesDataStore` delegate to manage
 * the "donetik_preferences" file.
 */
val datastoreModule = module {
    single<DataStore<Preferences>> { provideDataStore(get<Context>().applicationContext) }
}
package com.datahiveorg.donetik.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.datahiveorg.donetik.DoneTikApplication
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "donetik_preferences")

private fun provideDataStore(context: Context): DataStore<Preferences> = context.dataStore


val datastoreModule = module {
    single<DataStore<Preferences>> { provideDataStore(get<Context>().applicationContext) }
}
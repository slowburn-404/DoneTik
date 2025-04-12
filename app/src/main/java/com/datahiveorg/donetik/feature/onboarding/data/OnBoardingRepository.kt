package com.datahiveorg.donetik.feature.onboarding.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.datahiveorg.donetik.util.DispatcherProvider
import com.datahiveorg.donetik.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface OnBoardingRepository {
    val hasFinishedOnBoarding: Flow<Boolean>

    suspend fun setOnBoardingFinished(hasFinishedOnBoarding: Boolean)
}

class OnBoardingRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val dispatcherProvider: DispatcherProvider,
) : OnBoardingRepository {

    private val onboardingKey = booleanPreferencesKey("donetik_preferences")

    override val hasFinishedOnBoarding: Flow<Boolean>
        get() =
            try {
                dataStore.data.map { isOnBoardingFinished ->
                    isOnBoardingFinished[onboardingKey] ?: false
                }.flowOn(dispatcherProvider.io)
            } catch (exception: Exception) {
                Logger.e("DataStore", exception.message.toString())
                flowOf(false)
            }


    override suspend fun setOnBoardingFinished(hasFinishedOnBoarding: Boolean) {
        try {
            dataStore.edit { onboarding ->
                onboarding[onboardingKey] = hasFinishedOnBoarding
            }
        } catch (exception: Exception) {
            Logger.e("DataStore", exception.message.toString())
        }

    }
}
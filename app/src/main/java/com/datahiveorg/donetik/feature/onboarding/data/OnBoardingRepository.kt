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

/**
 * Repository interface for managing the on-boarding state of the application.
 * This interface defines the contract for accessing and modifying the on-boarding status.
 */
interface OnBoardingRepository {
    /**
     * A flow that emits a boolean value indicating whether the user has finished the onboarding process.
     * The value is retrieved from the DataStore. If the value is not found, it defaults to false.
     * If an exception occurs while retrieving the value, it defaults to false.
     */
    val hasFinishedOnBoarding: Flow<Boolean>

    /**
     * Sets the on-boarding finished status.
     *
     * @param hasFinishedOnBoarding True if the on-boarding process has been finished, false otherwise.
     */
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
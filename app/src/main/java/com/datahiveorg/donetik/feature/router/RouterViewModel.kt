package com.datahiveorg.donetik.feature.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.feature.onboarding.data.OnBoardingRepository
import com.datahiveorg.donetik.ui.navigation.AuthFeature
import com.datahiveorg.donetik.ui.navigation.HomeFeature
import com.datahiveorg.donetik.ui.navigation.OnBoardingFeature
import com.datahiveorg.donetik.util.Logger
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouterViewModel(
    private val authRepository: AuthRepository,
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<RouterState> = MutableStateFlow(RouterState())
    val state: StateFlow<RouterState> = _state.onStart {
        navigator()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RouterState()
    )

    private val _event: MutableSharedFlow<RouterEvent> = MutableSharedFlow()
    val event: SharedFlow<RouterEvent> = _event.asSharedFlow()

    private fun emitEvent(event: RouterEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private fun decideNavigation() {
        val currentState = _state.value

        when {
            !currentState.hasFinishedOnBoarding -> {
                emitEvent(RouterEvent.Navigate(OnBoardingFeature))
            }

            !currentState.isLoggedIn -> {
                emitEvent(RouterEvent.Navigate(AuthFeature))
            }

            else -> {
                emitEvent(RouterEvent.Navigate(HomeFeature))
            }
        }
    }

    private suspend fun navigator() = coroutineScope {
        launch {
            onBoardingRepository.hasFinishedOnBoarding.collect { isFinished ->
                _state.update { currentState ->
                    currentState.copy(hasFinishedOnBoarding = isFinished)
                }
                Logger.i("OnBoarding", isFinished.toString())
                decideNavigation()
            }
        }

        launch {
            val response = authRepository.checkLoginStatus()
            if (response is DomainResponse.Success) {
                _state.update { currentState ->
                    currentState.copy(
                        isLoggedIn = response.data
                    )
                }
                decideNavigation()
            }
        }

    }


}
package com.datahiveorg.donetik.feature.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.feature.onboarding.data.OnBoardingRepository
import com.datahiveorg.donetik.core.ui.navigation.AuthFeature
import com.datahiveorg.donetik.core.ui.navigation.HomeFeature
import com.datahiveorg.donetik.core.ui.navigation.OnBoardingFeature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouterViewModel(
    private val authRepository: AuthRepository,
    private val onBoardingRepository: OnBoardingRepository,
) : ViewModel() {

    init {
        navigator()
    }

    private val _state: MutableStateFlow<RouterState> = MutableStateFlow(RouterState())
    val state: StateFlow<RouterState> = _state.asStateFlow()

    private val _event = Channel<RouterEvent>()
    val event = _event.receiveAsFlow()

    //guard rails to ensure both async tasks complete
    private var isLoggedInFetched = false
    private var hasFinishedOnBoardingFetched = false

    private fun emitEvent(event: RouterEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }


    private fun decideNavigation() {
        if (!isLoggedInFetched || !hasFinishedOnBoardingFetched) return

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

    private fun navigator() {
        viewModelScope.launch {
            launch {
                onBoardingRepository.hasFinishedOnBoarding.collectLatest { isFinished ->
                    _state.update { currentState ->
                        currentState.copy(hasFinishedOnBoarding = isFinished)
                    }
                    hasFinishedOnBoardingFetched = true
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
                    isLoggedInFetched = true
                    decideNavigation()
                }
            }
        }
    }
}
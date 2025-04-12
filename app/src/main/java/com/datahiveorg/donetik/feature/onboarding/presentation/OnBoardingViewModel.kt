package com.datahiveorg.donetik.feature.onboarding.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.onboarding.data.OnBoardingRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val onBoardingRepository: OnBoardingRepository
) : ViewModel() {
    private val _state: MutableStateFlow<OnBoardingState> = MutableStateFlow(OnBoardingState())
    val state: StateFlow<OnBoardingState> = _state.asStateFlow()

    private val _events: MutableSharedFlow<OnBoardingEvents> = MutableSharedFlow(replay = 1)
    val events: SharedFlow<OnBoardingEvents> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            _events.collect { event ->
                when (event) {
                    is OnBoardingEvents.SetOnBoardingFinished -> setOnBoardingFinished(event.isFinished)
                    is OnBoardingEvents.None -> {}
                }
            }
        }
    }


    fun emitEvent(event: OnBoardingEvents) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }

    private suspend fun setOnBoardingFinished(hasFinishedOnBoarding: Boolean) {
        onBoardingRepository.setOnBoardingFinished(hasFinishedOnBoarding)
        _state.update { currentState ->
            currentState.copy(
                hasFinishedOnBoarding = hasFinishedOnBoarding
            )
        }
    }


}


@Immutable
@Stable
data class OnBoardingState(
    val pageIndex: Int = 0,
    val hasFinishedOnBoarding: Boolean = false
)

sealed interface OnBoardingEvents {
    data object None : OnBoardingEvents
    data class SetOnBoardingFinished(val isFinished: Boolean) : OnBoardingEvents
}
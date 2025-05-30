package com.datahiveorg.donetik.feature.onboarding.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.onboarding.data.OnBoardingRepository
import com.datahiveorg.donetik.ui.navigation.AuthFeature
import com.datahiveorg.donetik.ui.navigation.FeatureScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val onBoardingRepository: OnBoardingRepository
) : ViewModel() {
    private val _state: MutableStateFlow<OnBoardingState> = MutableStateFlow(OnBoardingState())
    val state: StateFlow<OnBoardingState> = _state.asStateFlow()


    private val _events = Channel<OnBoardingEvents>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private val _intents = MutableSharedFlow<OnBoardingIntents>()
    val intents = _intents.asSharedFlow()

    init {
        viewModelScope.launch {
            _intents.collect { event ->
                when (event) {
                    is OnBoardingIntents.SetOnBoardingFinished -> setOnBoardingFinished()
                }
            }
        }
    }


    private fun emitEvent(event: OnBoardingEvents) {
        viewModelScope.launch {
            _events.send(event)
        }
    }

    fun emitIntent(intent: OnBoardingIntents) {
        viewModelScope.launch {
            _intents.emit(intent)
        }
    }

    private suspend fun setOnBoardingFinished() {
        onBoardingRepository.setOnBoardingFinished(true)
        _state.update { currentState ->
            currentState.copy(
                hasFinishedOnBoarding = true
            )
        }
        if (_state.value.hasFinishedOnBoarding) emitEvent(OnBoardingEvents.Navigate(AuthFeature))
    }


}


@Immutable
@Stable
data class OnBoardingState(
    val pageIndex: Int = 0,
    val hasFinishedOnBoarding: Boolean = false,
    val pagerItems: List<PagerItems> = PagerItems.entries
)

sealed interface OnBoardingEvents {
    data class Navigate(val screen: FeatureScreen) : OnBoardingEvents

}

sealed interface OnBoardingIntents {
    data object SetOnBoardingFinished : OnBoardingIntents
}
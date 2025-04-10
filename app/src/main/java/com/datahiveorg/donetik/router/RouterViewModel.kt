package com.datahiveorg.donetik.router

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.domain.repository.AuthRepository
import com.datahiveorg.donetik.ui.navigation.AuthFeature
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RouterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            checkIfUserIsLoggedIn()
        }
    }

    private val _state: MutableStateFlow<RouterState> = MutableStateFlow(RouterState())
    //val state: StateFlow<RouterState> = _state.asStateFlow()

    private val _event: Channel<RouterEvent> = Channel(Channel.BUFFERED)
    val event: Flow<RouterEvent> = _event.receiveAsFlow()


    private suspend fun checkIfUserIsLoggedIn() {
        val response = authRepository.isUserLoggedIn()

        if (response is DomainResponse.Success) {
            _state.update { currentState ->
                currentState.copy(
                    isLoggedIn = response.data
                )
            }
            navigator()
        }
    }

    private fun emitEvent(event: RouterEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    private fun navigator() {
        val isLoggedIn = _state.value.isLoggedIn

        if (isLoggedIn) {
            emitEvent(RouterEvent.Navigate(AuthFeature))
        }
    }


}
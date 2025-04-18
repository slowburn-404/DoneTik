package com.datahiveorg.donetik.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
    private val userId: String?
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = HomeState(),
        started = WhileSubscribed(5000)
    ).onStart {
        emitIntent(HomeIntent.GetTasks)
    }

    private val _intent = MutableSharedFlow<HomeIntent>(replay = 1)
    private val intent = _intent.asSharedFlow()

    private val _event = MutableSharedFlow<HomeEvent>(replay = 1)
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            intent.collect { uiIntent ->
                when (uiIntent) {
                    is HomeIntent.GetTasks -> {
                        getTasks()
                    }
                }
            }
        }
    }


    fun emitIntent(intent: HomeIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    fun emitEvent(event: HomeEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private suspend fun getTasks() {
        showLoading()
        userId?.let { id ->
            when (val response = homeRepository.getTasks(id)) {
                is DomainResponse.Success -> {
                    _state.update { currentState ->
                        currentState.copy(
                            tasks = response.data,
                            isLoading = false
                        )
                    }
                }

                is DomainResponse.Failure -> {
                    _state.update { currentState ->
                        currentState.copy(
                            error = response.message,
                            isLoading = false
                        )
                    }
                    emitEvent(HomeEvent.ShowSnackBar(response.message))
                }
            }
        }

    }

    private fun showLoading() {
        _state.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
    }

}
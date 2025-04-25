package com.datahiveorg.donetik.feature.home.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.usecase.GetUserInfoUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val homeRepository: HomeRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = FeedState(),
        started = WhileSubscribed(5000)
    ).onStart {
        emitIntent(FeedIntent.GetUserInfo)
    }

    private val _intent = MutableSharedFlow<FeedIntent>()
    private val intent = _intent.asSharedFlow()

    private val _event = MutableSharedFlow<FeedEvent>()
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            intent.collect { uiIntent ->
                when (uiIntent) {
                    is FeedIntent.GetTasks -> {
                        getTasks(uiIntent.userId)
                    }

                    is FeedIntent.GetUserInfo -> {
                        getUserInfo()
                    }
                }
            }
        }
    }


    fun emitIntent(intent: FeedIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    fun emitEvent(event: FeedEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private suspend fun getTasks(userId: String) {
        showLoading()
        when (val response = homeRepository.getTasks(userId)) {
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
                emitEvent(FeedEvent.ShowSnackBar(response.message))
            }
        }
    }

    private suspend fun getUserInfo() {
        showLoading()
        when (val response = getUserInfoUseCase()) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        user = response.data,
                        isLoading = false
                    )
                }
                getTasks(_state.value.user.uid)
            }

            is DomainResponse.Failure -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false
                    )
                }
                emitEvent(FeedEvent.ShowSnackBar(response.message))
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
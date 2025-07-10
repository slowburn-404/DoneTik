package com.datahiveorg.donetik.feature.leaderboard.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.leaderboard.domain.LeaderBoardItem
import com.datahiveorg.donetik.feature.leaderboard.domain.LeaderBoardRepository
import com.datahiveorg.donetik.util.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LeaderBoardViewModel(
    private val leaderBoardRepository: LeaderBoardRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LeaderBoardState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LeaderBoardState()
    ).onStart {
        emitIntent(LeaderBoardIntent.FetchUserInfo)
        emitIntent(LeaderBoardIntent.FetchLeaderBoard)
    }

    private val _event = Channel<LeaderBoardEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _intent = MutableSharedFlow<LeaderBoardIntent>()
    val intent = _intent.asSharedFlow()


    init {
        handleIntents()
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.collectLatest { intent ->
                when (intent) {
                    LeaderBoardIntent.FetchLeaderBoard -> fetchLeaderBoard()
                    LeaderBoardIntent.FetchUserInfo -> fetchUserInfo()
                }
            }
        }
    }

    private fun emitEvent(event: LeaderBoardEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    fun emitIntent(intent: LeaderBoardIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }


    private suspend fun fetchLeaderBoard() {
        showLoadingIndicator()
        when (val response = leaderBoardRepository.fetchLeaderBoard()) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        leaderBoardItems = response.data
                    )
                }
                Logger.i("LeaderBoardItems", response.data.toString())
            }

            is DomainResponse.Error -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }
                showSnackBar(response.message)

            }
        }
    }

    private suspend fun fetchUserInfo() {
        showLoadingIndicator()
        when (val response = leaderBoardRepository.getCurrentUserInfo()) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        userId = response.data.uid
                    )
                }
            }

            is DomainResponse.Error -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false
                    )
                }
                showSnackBar(response.message)
            }
        }
    }

    private fun showLoadingIndicator() {
        _state.value = _state.value.copy(isLoading = true)
    }

    private fun showSnackBar(message: String) {
        emitEvent(LeaderBoardEvent.ShowSnackBar(message))
    }

}

data class LeaderBoardState(
    val isLoading: Boolean = false,
    val leaderBoardItems: List<LeaderBoardItem> = emptyList(),
    val userId: String = ""
)

sealed interface LeaderBoardIntent {
    object FetchLeaderBoard : LeaderBoardIntent
    object FetchUserInfo : LeaderBoardIntent
}

sealed interface LeaderBoardEvent {
    data class ShowSnackBar(val message: String) : LeaderBoardEvent
}
package com.datahiveorg.donetik.feature.home.presentation.newtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.feature.home.domain.usecase.GetUserInfoUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewTaskViewModel(
    private val homeRepository: HomeRepository, private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewTaskState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NewTaskState()
    ).onStart {
        emitIntent(NewTaskIntent.GetUserInfo)
    }

    private val _intent = MutableSharedFlow<NewTaskIntent>(replay = 1)
    val intent = _intent.asSharedFlow()

    private val _event = MutableSharedFlow<NewTaskEvent>(replay = 1)
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            intent.collect { newTaskIntent ->
                when (newTaskIntent) {
                    is NewTaskIntent.GetUserInfo -> {
                        getUserInfo()
                    }

                    is NewTaskIntent.CreateTask -> {
                        createNewTask(newTaskIntent.task)
                    }
                }
            }
        }
    }

    fun emitIntent(newTaskIntent: NewTaskIntent) {
        viewModelScope.launch {
            _intent.emit(newTaskIntent)
        }
    }

    fun emitEvent(newTaskEvent: NewTaskEvent) {
        viewModelScope.launch {
            _event.emit(newTaskEvent)
        }
    }

    private suspend fun getUserInfo() {
        when (val response = getUserInfoUseCase()) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        task = currentState.task.copy(author = response.data)
                    )
                }
            }

            is DomainResponse.Failure -> {
                _state.update { currentState ->
                    currentState.copy(
                        error = response.message
                    )
                }

                emitEvent(NewTaskEvent.ShowSnackBar(response.message))
            }
        }
    }

    private suspend fun createNewTask(task: Task) {
        when (val response = homeRepository.createTask(task)) {
            is DomainResponse.Success -> {
                emitEvent(NewTaskEvent.ShowSnackBar(response.data))
            }

            is DomainResponse.Failure -> {
                emitEvent(NewTaskEvent.ShowSnackBar(response.message))
            }
        }
    }
}
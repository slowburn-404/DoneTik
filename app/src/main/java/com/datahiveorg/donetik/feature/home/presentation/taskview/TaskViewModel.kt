package com.datahiveorg.donetik.feature.home.presentation.taskview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.model.Task
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(TaskViewState())
    val state = _state.asStateFlow()
    private val _intent = MutableSharedFlow<TaskViewIntent>(
        replay = 0,
        extraBufferCapacity = 1
    )
    val intent = _intent.asSharedFlow()

    private val _event = MutableSharedFlow<TaskViewEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            intent.collect { taskViewIntent ->
                when (taskViewIntent) {
                    is TaskViewIntent.GetTask -> {
                        getTask(taskId = taskViewIntent.taskId, userId = taskViewIntent.userId)
                    }

                    is TaskViewIntent.UpdateTask -> {
                        updateTask(taskViewIntent.task)
                    }

                    is TaskViewIntent.DeleteTask -> {
                        deleteTask(taskViewIntent.task)
                    }
                }
            }
        }
    }


    fun emitIntent(taskViewIntent: TaskViewIntent) {
        viewModelScope.launch {
            _intent.emit(taskViewIntent)
        }
    }

    fun emitEvent(taskViewEvent: TaskViewEvent) {
        viewModelScope.launch {
            _event.emit(taskViewEvent)
        }
    }

    private suspend fun getTask(userId: String, taskId: String) {
        when (val response =
            homeRepository.getSingleTask(taskId = taskId, userId = userId)) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        task = response.data
                    )
                }
            }

            is DomainResponse.Failure -> {
                _state.update { currentState ->
                    currentState.copy(
                        error = response.message
                    )
                }
                emitEvent(TaskViewEvent.ShowSnackBar(response.message))

            }
        }

    }

    private suspend fun updateTask(task: Task) {
        when (val response = homeRepository.updateTask((task))) {
            is DomainResponse.Success -> {
                emitEvent(TaskViewEvent.ShowSnackBar(response.data))
            }

            is DomainResponse.Failure -> {
                emitEvent(TaskViewEvent.ShowSnackBar(response.message))
            }
        }
    }

    private suspend fun deleteTask(task: Task) {
        when (val response = homeRepository.deleteTask((task))) {
            is DomainResponse.Success -> {
                emitEvent(TaskViewEvent.ShowSnackBar(response.data))
            }

            is DomainResponse.Failure -> {
                emitEvent(TaskViewEvent.ShowSnackBar(response.message))
            }
        }
    }
}
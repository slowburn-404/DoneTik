package com.datahiveorg.donetik.feature.home.presentation.taskview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.model.Task
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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
    private val intent = _intent.asSharedFlow()

    private val _event = Channel<TaskViewEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            intent.collect { taskViewIntent ->
                when (taskViewIntent) {
                    is TaskViewIntent.GetTask -> getTask(taskId = taskViewIntent.taskId, userId = taskViewIntent.userId)

                    is TaskViewIntent.UpdateTask -> updateTask(taskViewIntent.task)

                    is TaskViewIntent.DeleteTask -> deleteTask(taskViewIntent.task)

                    is TaskViewIntent.ToggleDoneStatus -> toggleDoneStatus(taskViewIntent.task)

                    is TaskViewIntent.ToggleBottomSheet -> toggleBottomSheet()
                }
            }
        }
    }


    fun emitIntent(taskViewIntent: TaskViewIntent) {
        viewModelScope.launch {
            _intent.emit(taskViewIntent)
        }
    }

    private fun emitEvent(taskViewEvent: TaskViewEvent) {
        viewModelScope.launch {
            _event.send(taskViewEvent)
        }
    }

    private suspend fun getTask(userId: String, taskId: String) {
        showLoadingIndicator()
        when (val response =
            homeRepository.getSingleTask(taskId = taskId, userId = userId)) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        task = response.data,
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
                emitEvent(TaskViewEvent.ShowSnackBar(response.message))

            }
        }

    }

    private suspend fun updateTask(task: Task) {
        showLoadingIndicator()
        when (val response = homeRepository.updateTask((task))) {
            is DomainResponse.Success -> {
                hideLoadingIndicator()
                emitEvent(TaskViewEvent.ShowSnackBar(response.data))
            }

            is DomainResponse.Failure -> {
                hideLoadingIndicator()
                emitEvent(TaskViewEvent.ShowSnackBar(response.message))
            }
        }
    }

    private suspend fun deleteTask(task: Task) {
        showLoadingIndicator()
        when (val response = homeRepository.deleteTask((task))) {
            is DomainResponse.Success -> {
                hideLoadingIndicator()
                emitEvent(TaskViewEvent.NavigateUp)
            }

            is DomainResponse.Failure -> {
                hideLoadingIndicator()
                emitEvent(TaskViewEvent.ShowSnackBar(response.message))
            }
        }
    }

    private suspend fun toggleDoneStatus(task: Task) {
        val newTask = task.copy(isDone = !task.isDone)
        showLoadingIndicator()
        when (val response = homeRepository.markTaskAsDone((newTask))) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        task = newTask,
                        isLoading = false
                    )
                }
                emitEvent(TaskViewEvent.ShowSnackBar(response.data))
            }

            is DomainResponse.Failure -> {
                hideLoadingIndicator()
                emitEvent(TaskViewEvent.ShowSnackBar(response.message))
            }
        }
    }

    private fun toggleBottomSheet() {
        _state.update { currentState ->
            currentState.copy(
                showBottomSheet = !currentState.showBottomSheet
            )
        }
    }

    private fun showLoadingIndicator() {
        _state.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
    }

    private fun hideLoadingIndicator() {
        _state.update { currentState ->
            currentState.copy(
                isLoading = false
            )
        }
    }
}
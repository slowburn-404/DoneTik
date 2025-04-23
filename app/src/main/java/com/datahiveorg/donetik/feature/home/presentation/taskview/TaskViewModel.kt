package com.datahiveorg.donetik.feature.home.presentation.taskview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val homeRepository: HomeRepository,
    private val taskId: String?
) : ViewModel() {

    private val _state = MutableStateFlow(TaskViewState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TaskViewState()
    ).onStart {
        taskId?.let {
            emitIntent(TaskViewIntent.GetTask(it))
        }
    }

    private val _intent = MutableSharedFlow<TaskViewIntent>(replay = 1)
    val intent = _intent.asSharedFlow()

    private val _event = MutableSharedFlow<TaskViewEvent>(replay = 1)
    val event = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            intent.collect { taskViewIntent ->
                when (taskViewIntent) {
                    is TaskViewIntent.GetTask -> {
                        getTask(taskViewIntent.taskId)
                    }

                    is TaskViewIntent.UpdateTask -> {
                        updateTask(taskViewIntent.taskId)
                    }

                    is TaskViewIntent.DeleteTask -> {
                        deleteTask(taskViewIntent.taskId)
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

    private suspend fun getTask(taskId: String) {
        when (val response = homeRepository.updateTask((taskId))) {
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

    private suspend fun updateTask(taskId: String) {
        when (val response = homeRepository.updateTask((taskId))) {
            is DomainResponse.Success -> {
                emitEvent(TaskViewEvent.ShowSnackBar(response.data))
            }

            is DomainResponse.Failure -> {
                emitEvent(TaskViewEvent.ShowSnackBar(response.message))
            }
        }
    }

    private suspend fun deleteTask(taskId: String) {
        when (val response = homeRepository.deleteTask((taskId))){
            is DomainResponse.Success -> {
                emitEvent(TaskViewEvent.ShowSnackBar(response.data))
            }
            is DomainResponse.Failure -> {
                emitEvent(TaskViewEvent.ShowSnackBar(response.message))
            }
        }
    }
}
package com.datahiveorg.donetik.feature.home.presentation.newtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.usecase.GetUserInfoUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewTaskViewModel(
    private val homeRepository: HomeRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewTaskState())
    val state = _state.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NewTaskState()
    ).onStart {
        emitIntent(NewTaskIntent.GetUserInfo)
    }

    private val _intent = MutableSharedFlow<NewTaskIntent>()
    private val intent = _intent.asSharedFlow()

    private val _event = Channel<NewTaskEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            intent.collect { newTaskIntent ->
                when (newTaskIntent) {
                    is NewTaskIntent.GetUserInfo -> {
                        getUserInfo()
                    }

                    is NewTaskIntent.CreateTask -> {
                        createNewTask()
                    }

                    is NewTaskIntent.EnterTitle -> {
                        enterTitle(newTaskIntent.title)
                    }

                    is NewTaskIntent.EnterDescription -> {
                        enterDescription(newTaskIntent.description)
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
            _event.send(newTaskEvent)
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

    private suspend fun createNewTask() {
        showLoading()
        val task = _state.value.task
        when (val response = homeRepository.createTask(task)) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false
                    )
                }
                hideLoading()
                emitEvent(NewTaskEvent.ShowSnackBar(response.data))
                emitEvent(NewTaskEvent.SaveSuccessful)
            }

            is DomainResponse.Failure -> {
                hideLoading()
                emitEvent(NewTaskEvent.ShowSnackBar(response.message))
            }
        }
    }

    private fun enterTitle(title: String) {
        _state.update { currentState ->
            currentState.copy(
                task = currentState.task.copy(title = title)
            )
        }
        validateForm()
    }

    private fun enterDescription(description: String) {
        _state.update { currentState ->
            currentState.copy(
                task = currentState.task.copy(description = description)
            )
        }
        validateForm()
    }

    private fun validateForm() {
        val state = _state.value
        _state.update { currentState ->
            currentState.copy(
                isFormValid = state.task.title.isNotEmpty() &&
                        state.task.description.isNotEmpty(),
                titleError = if (state.task.title.isEmpty()) "Title cannot be empty" else "",
                descriptionError = if (state.task.description.isEmpty()) "Description cannot be empty" else ""
            )
        }
    }

    private fun showLoading() {
        _state.update { currentState ->
            currentState.copy(
                isLoading = true
            )
        }
    }

    private fun hideLoading() {
        _state.update { currentState ->
            currentState.copy(
                isLoading = false
            )
        }
    }
}
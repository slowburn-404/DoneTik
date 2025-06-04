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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    private val _intent = MutableSharedFlow<NewTaskIntent>(extraBufferCapacity = 64)
    private val intent = _intent.asSharedFlow()

    private val _event = Channel<NewTaskEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            intent.collect { newTaskIntent ->
                when (newTaskIntent) {
                    is NewTaskIntent.GetUserInfo -> getUserInfo()

                    is NewTaskIntent.CreateTask -> createNewTask()

                    is NewTaskIntent.EnterTitle -> enterTitle(newTaskIntent.title)

                    is NewTaskIntent.EnterDescription -> enterDescription(newTaskIntent.description)

                    is NewTaskIntent.EnterCategory -> enterCategory(newTaskIntent.category)

                    is NewTaskIntent.ToggleDialog -> toggleCategoryDialog()

                    is NewTaskIntent.EnterDate -> enterDate(newTaskIntent.date)

                    is NewTaskIntent.EnterTime -> enterTime(
                        hour = newTaskIntent.hour,
                        minute = newTaskIntent.minute
                    )

                    is NewTaskIntent.ToggleTimePicker -> toggleTimePicker()

                    is NewTaskIntent.ToggleDatePicker -> toggleDatePicker()

                }
            }
        }
    }

    fun emitIntent(newTaskIntent: NewTaskIntent) {
        viewModelScope.launch {
            _intent.emit(newTaskIntent)
        }
    }

    private fun emitEvent(newTaskEvent: NewTaskEvent) {
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
                        state.task.description.isNotEmpty() &&
                        state.selectedDate != null,
                titleError = if (state.task.title.isEmpty()) "Title cannot be empty" else "",
                descriptionError = if (state.task.description.isEmpty()) "Description cannot be empty" else "",
                selectedDateError = if (state.selectedDate == null) "Date cannot be empty" else "",
                selectedTimeError = if (state.selectedHour == null || state.selectedMinute == null) "Time cannot be empty" else ""
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

    private fun toggleCategoryDialog() {
        _state.update { currentState ->
            currentState.copy(
                showCategoryDialog = !currentState.showCategoryDialog
            )
        }
    }

    private fun enterCategory(category: String) {
        _state.update { currentState ->
            currentState.copy(
                task = currentState.task.copy(category = category)
            )
        }
    }

    private fun enterDate(date: Long) {
        _state.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
        combineDateTime()
    }

    private fun enterTime(hour: Int, minute: Int) {
        _state.update { currentState ->
            currentState.copy(
                selectedHour = hour,
                selectedMinute = minute
            )
        }
        combineDateTime()
    }

    private fun combineDateTime() {
        val state = _state.value
        val selectedDate = state.selectedDate
        val selectedHour = state.selectedHour
        val selectedMinute = state.selectedMinute
        val outPutDateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())

        if (selectedDate != null && selectedMinute != null && selectedHour != null) {
            try {
                val dateCalendar = Calendar.getInstance()
                dateCalendar.timeInMillis = selectedDate
                dateCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                dateCalendar.set(Calendar.MINUTE, selectedMinute)
                dateCalendar.set(Calendar.SECOND, 0)
                dateCalendar.set(Calendar.MILLISECOND, 0)
                val combinedDateTime = outPutDateFormat.format(dateCalendar.time)

                _state.update { currentState ->
                    currentState.copy(
                        task = currentState.task.copy(dueDate = combinedDateTime),
                    )
                }

            } catch (e: Exception) {
                _state.update { currentState ->
                    currentState.copy(
                        task = currentState.task.copy(dueDate = ""),
                    )

                }
                emitEvent(NewTaskEvent.ShowSnackBar("Could not process due date, kindly try again"))
            }
        } else {
            _state.update { currentState ->
                currentState.copy(
                    task = currentState.task.copy(dueDate = ""),
                )
            }
        }
        validateForm()
    }

    private fun toggleDatePicker() {
        _state.update { currentState ->
            currentState.copy(
                showDatePicker = !currentState.showDatePicker
            )
        }
    }

    private fun toggleTimePicker() {
        _state.update { currentState ->
            currentState.copy(
                showTimePicker = !currentState.showTimePicker
            )
        }
    }
}
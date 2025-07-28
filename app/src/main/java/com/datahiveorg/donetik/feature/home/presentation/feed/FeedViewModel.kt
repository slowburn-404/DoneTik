package com.datahiveorg.donetik.feature.home.presentation.feed

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.feature.home.domain.usecase.GetUserInfoUseCase
import com.datahiveorg.donetik.util.DispatcherProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedViewModel(
    private val homeRepository: HomeRepository,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(FeedState())
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = FeedState(),
        started = WhileSubscribed(5000)
    ).onStart {
        emitIntent(FeedIntent.GetUserInfo)
    }

    private val _pendingTasks = MutableStateFlow(emptyList<Task>())
    val pendingTasks = _pendingTasks.stateIn(
        scope = viewModelScope,
        initialValue = emptyList(),
        started = WhileSubscribed(5000)
    ).onStart {
        emitIntent(FeedIntent.GetPendingTasks)
    }.flowOn(dispatcher.default)

    private val _intent = MutableSharedFlow<FeedIntent>(extraBufferCapacity = 64)
    private val intent = _intent.asSharedFlow()

    private val _event = Channel<FeedEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            intent.collect { uiIntent ->
                when (uiIntent) {
                    is FeedIntent.GetTasks -> getTasks(uiIntent.userId)

                    is FeedIntent.GetUserInfo -> getUserInfo()

                    is FeedIntent.Delete -> deleteTask(uiIntent.task)

//                    is FeedIntent.ToggleDoneStatus -> toggleDoneStatus(uiIntent.task)

                    is FeedIntent.Refresh -> getUserInfo()

                    is FeedIntent.GetPendingTasks -> getPendingTasks()

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
            _event.send(event)
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

            is DomainResponse.Error -> {
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
                generateGreetingText()
                getTasks(_state.value.user.uid)
                getCarouselItems()

            }

            is DomainResponse.Error -> {
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



    private fun generateGreetingText() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val username = _state.value.user.username
        val greeting = when (currentHour) {
            in 5..11 -> "Good morning, $username"
            in 12..16 -> "Good afternoon, $username"
            in 17..20 -> "Good evening, $username"
            else -> "Hello, $username"
        }

        _state.update { currentState ->
            currentState.copy(title = greeting)
        }
    }

    private suspend fun deleteTask(task: Task) {
        showLoading()
        when (val response = homeRepository.deleteTask(task)) {
            is DomainResponse.Success -> {
                _state.update { currentState ->
                    val newTaskList = currentState.tasks.filter { it.id != task.id }
                    currentState.copy(
                        tasks = newTaskList,
                        isLoading = false
                    )
                }
                getCarouselItems()
                emitEvent(FeedEvent.ShowSnackBar("Task deleted"))
            }

            is DomainResponse.Error -> {
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

    //TODO(create a use case)
//    private suspend fun toggleDoneStatus(task: Task) {
//        val newTask = task.copy(isDone = !task.isDone)
//        showLoading()
//        when (val response = homeRepository.markTaskAsDone(newTask)) {
//            is DomainResponse.Success -> {
//                _state.update { currentState ->
//                    val newTaskList = currentState.tasks.map {
//                        if (it.id == newTask.id) {
//                            newTask
//                        } else {
//                            it
//                        }
//                    }
//                    currentState.copy(
//                        isLoading = false,
//                        tasks = newTaskList
//                    )
//                }
//                filterByDone(_filteredTasks.value.filter)
//                emitEvent(FeedEvent.ShowSnackBar("Done status changed"))
//            }
//
//            is DomainResponse.Failure -> {
//                _state.update { currentState ->
//                    currentState.copy(
//                        error = response.message,
//                        isLoading = false
//                    )
//                }
//                emitEvent(FeedEvent.ShowSnackBar(response.message))
//            }
//
//        }
//    }
//
    private fun getCarouselItems() {
        viewModelScope.launch(dispatcher.default) {
            val allTasks = _state.value.tasks
            val carouselItems =
                allTasks
                    //.take(3)
                    .asSequence()
                    .sortedByDescending { it.createdAt }
                    .groupBy { it.category }
                    .map { (category, tasksInCategory) ->
                        CarouselItem(
                            category = category,
                            count = tasksInCategory.count(),
                            contentDescription = category
                        )
                    }.toSet()

            _state.update { currentState ->
                currentState.copy(
                    carouselItems = carouselItems
                )
            }
        }
    }

    private fun getPendingTasks() {
        val allTasks = _state.value.tasks
        _pendingTasks.update {
            allTasks
                .filter {
                    !it.isDone
                }
        }
    }
}
package com.datahiveorg.donetik.feature.home.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.home.domain.HomeRepository
import com.datahiveorg.donetik.feature.home.domain.model.Task
import com.datahiveorg.donetik.util.DispatcherProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


typealias GroupedTasks = Map<String, List<Task>>

class TaskListViewModel(
    private val homeRepository: HomeRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    private val _state = MutableStateFlow(TaskListState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5_000),
        initialValue = TaskListState()
    ).onStart {
        emitIntent(TaskListIntent.GetTasks)
    }

    private val _event = Channel<TaskListEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _intent = MutableSharedFlow<TaskListIntent>()
    val intent = _intent.asSharedFlow()

    init {
        viewModelScope.launch {
            intent.collectLatest { intent ->
                handleIntent(intent)
            }
        }

        viewModelScope.launch {
            emitIntent(TaskListIntent.GetUserInfo)
        }
    }

    fun emitIntent(intent: TaskListIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun emitEvent(event: TaskListEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    private suspend fun handleIntent(intent: TaskListIntent) {
        when (intent) {
            is TaskListIntent.GetTasks -> getTasks()

            is TaskListIntent.GetUserInfo -> getUserInfo()

            is TaskListIntent.SelectCategory -> updateSelectedCategory(intent.category)

            is TaskListIntent.Filter -> updateFilterOption(intent.filter)

            is TaskListIntent.SelectTask -> selectTask(intent.task)
        }

    }


    private suspend fun getUserInfo() {
        showLoadingIndicator()
        when (val response = homeRepository.getCurrentUserInfo()) {
            is DomainResponse.Error -> {
                _state.update { currentState ->
                    currentState.copy(isLoading = false)
                }
                emitEvent(TaskListEvent.ShowSnackBar(response.message))
            }

            is DomainResponse.Success -> {
                _state.update { currentState ->
                    currentState.copy(
                        userId = response.data.uid,
                        isLoading = false
                    )
                }
            }
        }
    }


    private suspend fun getTasks() {
        showLoadingIndicator()
        val userId = _state.value.userId
        if (userId.isEmpty()) return
        when (val response = homeRepository.getTasks(userId)) {
            is DomainResponse.Error -> {
                _state.update { currentState ->
                    currentState.copy(isLoading = false)
                }
                emitEvent(TaskListEvent.ShowSnackBar(response.message))
            }

            is DomainResponse.Success -> {
                _state.update { currentState ->
                    val newAllTasks = response.data
                    val categories = calculateCategories(newAllTasks)
                    val newDisplayedTasks = calculateDisplayedTasks(
                        newAllTasks,
                        currentState.currentFilterOption,
                        currentState.selectedCategory
                    )
                    currentState.copy(
                        allTasks = newAllTasks,
                        categories = categories,
                        displayedTasks = newDisplayedTasks,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun showLoadingIndicator() {
        _state.update { currentState ->
            currentState.copy(isLoading = true)
        }
    }

    private fun updateSelectedCategory(category: String) {
        _state.update { currentState ->
            val displayedTasks = calculateDisplayedTasks(
                currentState.allTasks,
                currentState.currentFilterOption,
                category
            )
            currentState.copy(
                selectedCategory = category,
                displayedTasks = displayedTasks
            )
        }
    }


    private fun updateFilterOption(filterOption: FilterOption) {
        _state.update { currentState ->
            val displayedTasks = calculateDisplayedTasks(
                currentState.allTasks,
                filterOption,
                currentState.selectedCategory
            )
            currentState.copy(
                currentFilterOption = filterOption,
                displayedTasks = displayedTasks
            )
        }
    }

    private fun groupByDate(tasks: List<Task>): Map<String, List<Task>> =
        tasks.groupBy { it.createdAt.substringBefore(",") }

    private fun selectTask(task: Task) {
        _state.update { currentState ->
            currentState.copy(selectedTask = task)
        }
        val task = _state.value.selectedTask
        task?.let {
            emitEvent(TaskListEvent.NavigateToTask(taskId = it.id, userId = it.author))
        }
    }

    private fun calculateCategories(tasks: List<Task>): Set<String> {
        return tasks.map { it.category }.toSet()
    }

    private fun calculateDisplayedTasks(
        allTasks: List<Task>,
        filter: FilterOption,
        selectedCategory: String
    ): GroupedTasks {
        val filteredByOption = when (filter) {
            FilterOption.PENDING -> allTasks.filter { !it.isDone }
            FilterOption.DONE -> allTasks.filter { it.isDone }
            FilterOption.ALL -> {
                allTasks
            }
        }
        val filteredByCategory = if (selectedCategory.isNotEmpty()) {
            filteredByOption.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        } else {
            filteredByOption
        }

        return groupByDate(filteredByCategory)
    }
}
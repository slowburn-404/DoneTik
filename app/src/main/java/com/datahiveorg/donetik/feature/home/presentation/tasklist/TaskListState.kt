package com.datahiveorg.donetik.feature.home.presentation.tasklist

import com.datahiveorg.donetik.feature.home.domain.model.Task

data class TaskListState(
    val isLoading: Boolean = false,
    val allTasks: List<Task> = emptyList(),
    val userId: String = "",
    val categories: Set<String> = emptySet(),
    val selectedCategory: String = "",
    val selectedTask: Task? = null,
    val displayedTasks: GroupedTasks = emptyMap(),
    val currentFilterOption: FilterOption = FilterOption.ALL
)

enum class FilterOption {
    ALL, PENDING, DONE
}


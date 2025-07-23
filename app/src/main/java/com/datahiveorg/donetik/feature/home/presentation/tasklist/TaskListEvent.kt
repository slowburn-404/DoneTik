package com.datahiveorg.donetik.feature.home.presentation.tasklist

interface TaskListEvent {
    data class ShowSnackBar(val message: String) : TaskListEvent
    data class NavigateToTask(val taskId: String, val userId: String) : TaskListEvent
    data class SelectCategory(val category: String) : TaskListEvent
}
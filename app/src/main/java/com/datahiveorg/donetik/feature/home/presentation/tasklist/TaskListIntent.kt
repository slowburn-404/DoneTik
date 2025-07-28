package com.datahiveorg.donetik.feature.home.presentation.tasklist

import com.datahiveorg.donetik.feature.home.domain.model.Task

interface TaskListIntent {
    data object GetTasks : TaskListIntent
    data object GetUserInfo : TaskListIntent
    data class SelectCategory(val category: String) : TaskListIntent
    data class Filter(val filter: FilterOption) : TaskListIntent
    data class SelectTask(val task: Task): TaskListIntent
    data object Search: TaskListIntent
}
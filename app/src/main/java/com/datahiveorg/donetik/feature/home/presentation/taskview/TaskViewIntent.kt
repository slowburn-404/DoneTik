package com.datahiveorg.donetik.feature.home.presentation.taskview

import com.datahiveorg.donetik.feature.home.domain.model.Task

interface TaskViewIntent {
    data class GetTask(val taskId: String, val userId: String) : TaskViewIntent
    data class UpdateTask(val task: Task) : TaskViewIntent
    data class DeleteTask(val task: Task) : TaskViewIntent
    data class ToggleDoneStatus(val task: Task) : TaskViewIntent
    data object ToggleBottomSheet: TaskViewIntent
}
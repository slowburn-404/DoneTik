package com.datahiveorg.donetik.feature.home.presentation.taskview

import com.datahiveorg.donetik.feature.home.domain.model.Task

interface TaskViewIntent {
    data class GetTask(val taskId: String) : TaskViewIntent
    data class UpdateTask(val taskId: String) : TaskViewIntent
    data class DeleteTask(val taskId: String) : TaskViewIntent
}
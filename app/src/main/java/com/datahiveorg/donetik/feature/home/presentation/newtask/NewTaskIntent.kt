package com.datahiveorg.donetik.feature.home.presentation.newtask

import com.datahiveorg.donetik.feature.home.domain.model.Task

sealed interface NewTaskIntent {
    data object GetUserInfo : NewTaskIntent
    data class CreateTask(val task: Task) : NewTaskIntent
}
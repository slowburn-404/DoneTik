package com.datahiveorg.donetik.feature.home.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.datahiveorg.donetik.feature.auth.domain.model.User
import com.datahiveorg.donetik.feature.home.domain.model.Task

@Stable
@Immutable
data class HomeState(
    val tasks: List<Task> = emptyList(),
    val selectedTask: Task? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val user: User? = null
)

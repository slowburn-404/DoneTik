package com.datahiveorg.donetik.feature.home.presentation

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.datahiveorg.donetik.feature.home.domain.model.Task

@Immutable
@Stable
data class HomeState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false
)
package com.datahiveorg.donetik.feature.home.presentation

sealed interface HomeEvent {
    data object CreateTask: HomeEvent
    data object GetTasks: HomeEvent
    data object DeleteTask: HomeEvent
}
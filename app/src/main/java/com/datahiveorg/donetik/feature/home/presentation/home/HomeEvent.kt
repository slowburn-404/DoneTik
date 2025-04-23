package com.datahiveorg.donetik.feature.home.presentation.home

interface HomeEvent {
    data object CreateTask: HomeEvent
    data class SelectTask(val taskId: String) : HomeEvent
    data class ShowSnackBar(val message: String) : HomeEvent
}
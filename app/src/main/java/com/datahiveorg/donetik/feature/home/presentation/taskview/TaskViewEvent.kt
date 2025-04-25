package com.datahiveorg.donetik.feature.home.presentation.taskview

interface TaskViewEvent {
    data class ShowSnackBar(val message: String) : TaskViewEvent
}
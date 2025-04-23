package com.datahiveorg.donetik.feature.home.presentation.newtask

sealed interface NewTaskEvent {
    data class ShowSnackBar(val message: String) : NewTaskEvent
}
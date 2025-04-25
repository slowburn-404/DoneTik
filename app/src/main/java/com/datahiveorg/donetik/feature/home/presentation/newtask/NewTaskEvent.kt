package com.datahiveorg.donetik.feature.home.presentation.newtask

sealed interface NewTaskEvent {
    data object None : NewTaskEvent
    data class ShowSnackBar(val message: String) : NewTaskEvent
    data object SaveSuccessful: NewTaskEvent
}
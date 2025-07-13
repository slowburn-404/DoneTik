package com.datahiveorg.donetik.feature.home.presentation.taskview

import com.datahiveorg.donetik.core.ui.navigation.FeatureScreen

interface TaskViewEvent {
    data class ShowSnackBar(val message: String) : TaskViewEvent
    data object NavigateUp: TaskViewEvent
}
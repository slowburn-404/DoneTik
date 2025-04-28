package com.datahiveorg.donetik.feature.home.presentation.taskview

import com.datahiveorg.donetik.ui.navigation.FeatureScreen

interface TaskViewEvent {
    data class ShowSnackBar(val message: String) : TaskViewEvent
    data object MarkAsDone : TaskViewEvent
    data class Navigate(val screen: FeatureScreen) : TaskViewEvent
}
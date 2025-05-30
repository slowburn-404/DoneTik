package com.datahiveorg.donetik.feature.home.presentation.newtask

sealed interface NewTaskIntent {
    data object GetUserInfo : NewTaskIntent
    data object CreateTask : NewTaskIntent
    data class EnterTitle(val title: String) : NewTaskIntent
    data class EnterDescription(val description: String) : NewTaskIntent
    data class EnterCategory(val category: String) : NewTaskIntent
    data object ToggleDialog : NewTaskIntent
    data class EnterDate(val date: Long) : NewTaskIntent
    data class EnterTime(val hour: Int, val minute: Int) : NewTaskIntent
    data object ToggleTimePicker: NewTaskIntent
    data object ToggleDatePicker: NewTaskIntent
}
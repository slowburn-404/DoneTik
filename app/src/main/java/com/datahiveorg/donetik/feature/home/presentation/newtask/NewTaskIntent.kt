package com.datahiveorg.donetik.feature.home.presentation.newtask

sealed interface NewTaskIntent {
    data object GetUserInfo : NewTaskIntent
    data object CreateTask : NewTaskIntent
    data class EnterTitle(val title: String) : NewTaskIntent
    data class EnterDescription(val description: String) : NewTaskIntent
    data class EnterCategory(val category: String) : NewTaskIntent
    data object ToggleDialog: NewTaskIntent
}
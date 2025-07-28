package com.datahiveorg.donetik.feature.home.presentation.feed

import com.datahiveorg.donetik.feature.home.presentation.tasklist.FilterOption

interface FeedEvent {
    data class ShowSnackBar(val message: String) : FeedEvent
    data class SelectTask(val taskId: String, val userId: String) : FeedEvent
    data object None : FeedEvent
    sealed interface Navigate {
        data object Feed : FeedEvent
        data object NewTask : FeedEvent
        data class TaskList(val category: String, val filterOption: FilterOption) : FeedEvent
    }

    data object ToggleOptionsBottomSheet : FeedEvent
}
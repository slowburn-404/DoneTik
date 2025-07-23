package com.datahiveorg.donetik.feature.home.presentation.feed

import com.datahiveorg.donetik.feature.home.domain.model.Task

interface FeedIntent {
    data class GetTasks(val userId: String) : FeedIntent
    data object GetUserInfo : FeedIntent
//    data class ToggleDoneStatus(val task: Task) : FeedIntent
    data class Delete(val task: Task) : FeedIntent
    data object ToggleSearchBar : FeedIntent
    data class EnterQuery(val query: String) : FeedIntent
    data object Search : FeedIntent
    data object Refresh : FeedIntent
    data object GetPendingTasks: FeedIntent
}
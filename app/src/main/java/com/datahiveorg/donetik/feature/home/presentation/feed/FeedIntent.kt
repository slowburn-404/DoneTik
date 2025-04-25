package com.datahiveorg.donetik.feature.home.presentation.feed

interface FeedIntent {
    data class GetTasks(val userId: String) : FeedIntent
    data object GetUserInfo: FeedIntent
    data object CreateTask : FeedEvent
}
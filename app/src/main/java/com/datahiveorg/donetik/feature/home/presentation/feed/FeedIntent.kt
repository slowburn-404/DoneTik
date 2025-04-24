package com.datahiveorg.donetik.feature.home.presentation.feed

interface FeedIntent {
    data object GetTasks : FeedIntent
    data object CreateTask : FeedEvent
}
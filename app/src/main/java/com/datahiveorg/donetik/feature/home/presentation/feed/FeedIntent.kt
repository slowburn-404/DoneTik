package com.datahiveorg.donetik.feature.home.presentation.feed

import com.datahiveorg.donetik.feature.home.domain.model.Task

interface FeedIntent {
    data class GetTasks(val userId: String) : FeedIntent
    data object GetUserInfo : FeedIntent
    data class Filter(val filter: Status) : FeedIntent
}